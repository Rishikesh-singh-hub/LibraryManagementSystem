package com.microservice.library.service;

import com.microservice.grpc.book.*;
import com.microservice.grpc.user.IssueUserRequest;

import com.microservice.grpc.user.UserRequest;
import com.microservice.grpc.user.UserResponse;
import com.microservice.grpc.user.UserServiceGrpc;
import com.microservice.library.dto.*;
import com.microservice.library.entity.LibraryCriteria;
import com.microservice.library.entity.LibraryRecord;
import com.microservice.library.jwt.JwtUtils;
import com.microservice.library.repository.LibraryRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Lazy
public class LibraryService {

    Logger logger = LoggerFactory.getLogger(LibraryService.class);

    private final LibraryRepo libraryRepo;
    private final BookServiceGrpc.BookServiceBlockingStub bookStub;
    private UserServiceGrpc.UserServiceBlockingStub userStub;
    private final LibraryMapper libraryMapper;
    private final JwtUtils jwtUtils;
    private final LibraryCriteria libraryCriteria;
    // ------------------ constructor

    public LibraryService(LibraryRepo libraryRepo, LibraryMapper libraryMapper
            , @Qualifier("bookStub") BookServiceGrpc.BookServiceBlockingStub bookStub
            , @Qualifier("userStub") UserServiceGrpc.UserServiceBlockingStub userStub
            , JwtUtils jwtUtils, LibraryCriteria libraryCriteria) {
        this.libraryRepo = libraryRepo;

        this.bookStub = bookStub;
        this.userStub = userStub;
        this.libraryMapper = libraryMapper;

        this.jwtUtils = jwtUtils;
        this.libraryCriteria = libraryCriteria;
    }


    // -----------functions





    public ResponseEntity<?> getAllBooks() {

        logger.info("getting all books from server");
        BookListResponse response ;
        List<LibraryBookDto> libraryBookDtos ;
        try {
            response = bookStub.getAllBooks(EmptyRequest.newBuilder().build());
            logger.info(" total books received {} ", response.getBooksCount());
            libraryBookDtos = response.getBooksList()
                    .stream()
                    .map(libraryMapper::toLibraryBookDto)
                    .toList();
            return ResponseEntity.ok(libraryBookDtos.stream().map(libraryMapper::toBookResponseDto).toList());


        } catch (Exception e) {
            logger.info(e.getMessage());
        }


        return ResponseEntity.badRequest().build();

    }

    public ResponseEntity<?> issueBook(List<String>ids , HttpServletRequest request) {



        String jwt = request.getHeader("Authorization").substring(7);
        logger.info("got jwt {}",jwt);

        String userId = jwtUtils.getUserIdFromJwt(jwt);
        UserResponse response =userStub.getUserById(UserRequest.newBuilder().setId(userId).build());


        if( response.getBooksIssuedCount() >= response.getMaxBooksAllowed()) {
            return ResponseEntity.badRequest().body("Book issue limit reached ");
        }else if ( response.getHasPendingFines() ){
            return ResponseEntity.badRequest().body("Return fine first");
        } else if (response.getBooksIssuedCount() + ids.size() > response.getMaxBooksAllowed()) {
            return ResponseEntity.badRequest().body("limit reached return some books");
        }



        List<LibraryRecord> libraryRecord = ids.stream().map(id -> LibraryRecord.builder()
                                                            .userId(userId)
                                                            .bookId(id)
                                                            .returned(false)
                                                            .status("ISSUED")
                                                            .build()).toList();
        libraryRepo.saveAll(libraryRecord);
        logger.info("Total books issued {}",libraryRecord.size());

        UserResponse updatedResponse = userStub.issueBooksUpdate(IssueUserRequest.newBuilder()
                    .setId(userId)
                    .setCount(ids.size())
                    .setIssue(true)
                    .build());
        logger.info("User updated");


        bookStub.issueBook(IssueBookRequest.newBuilder().addAllBookId(ids).setIssueBooks(true).build());


        UserResponseDto userResponseDto = libraryMapper.toUserResponseDto(updatedResponse);
        return ResponseEntity.ok().body(userResponseDto);

    }

    public ResponseEntity<?> returnBooks( IssueRequestDto requestDto,HttpServletRequest req) {

        String jwt = req.getHeader("Authorization").substring(7);

        String userId = jwtUtils.getUserIdFromJwt(jwt);
        logger.info("got id {}",userId);



        int listSize = requestDto.getBid().size();


        List<String> bIds = requestDto.getBid();

        logger.info("total request to update :{}",bIds.size());

        List<LibraryRecord> libraryRecord = bIds.stream().map(
                bId -> libraryCriteria
                        .getRecordById(userId,bId)
        ).toList();

        logger.info("total records found: {}",libraryRecord.size());
        if(!libraryRecord.isEmpty())
            libraryRecord.forEach(record -> record.setReturned(true));


        libraryRepo.saveAll(libraryRecord);
        logger.info("total books returned {}",libraryRecord.size());
        IssueUserRequest requestCarrier = IssueUserRequest.newBuilder()
                .setCount(listSize)
                .setId(userId)
                .setIssue(false)
                .build();
        logger.info("update user id");

        UserResponse userResponse = userStub.issueBooksUpdate(requestCarrier);
        bookStub.issueBook(IssueBookRequest.newBuilder().addAllBookId(requestDto.getBid()).setIssueBooks(false).build());

        return ResponseEntity.ok(libraryMapper.toUserResponseDto(userResponse));

    }

    public ResponseEntity<?> bookNotReturned(HttpServletRequest req) {

        String jwt = req.getHeader("Authorization").substring(7);
        logger.info("got jwt {}",jwt);

        String userId = jwtUtils.getUserIdFromJwt(jwt);

        List<String> bookIds = libraryCriteria.getRecordById(userId);

        List<BookResponse> bookListResponse= bookIds.stream().map(
                bookId ->bookStub.getBookById(BookIdRequest.newBuilder().setBookId(bookId).build())
        ).toList();

        List<BookResponseDto> bookResponseDtos = bookListResponse.stream().map(libraryMapper::toBookResponseDto).toList();
        return ResponseEntity.ok(bookResponseDtos);

    }


    public void saveBook(@Valid List<BookResponseDto> bookDtos) {

        logger.info("received request to save {} books",bookDtos.size());
        List<CreateBookRequest> bookResponses = bookDtos.stream().map(
                libraryMapper::toCreateBookObj
        ).toList();

        CreateListBook listBook = CreateListBook.newBuilder().addAllBookRequest(bookResponses).build();

        bookStub.addBook(listBook);



    }
}
