package com.microservice.service;

import com.microservice.grpc.book.*;
import com.microservice.grpc.book.BookServiceGrpc.BookServiceImplBase;
import com.microservice.dto.BookMapper;
import com.microservice.entity.BookEntity;
import com.microservice.exception.UsernameNotFoundException;
import com.microservice.repository.BookCriteria;
import com.microservice.repository.BookRepo;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@GrpcService
public class BookGrpcServer extends BookServiceImplBase {

    static Logger logger = LoggerFactory.getLogger(BookGrpcServer.class);



    private final BookCriteria bookCriteria;
    private final BookRepo bookRepo;
    private final BookMapper bookMapper;






    public BookGrpcServer(BookCriteria bookCriteria, BookRepo bookRepo, BookMapper bookMapper) {
        this.bookCriteria = bookCriteria;
        this.bookRepo = bookRepo;
        this.bookMapper = bookMapper;
    }



    @Override
    public void getBookById(BookIdRequest request,
                            StreamObserver<BookResponse> responseObserver){

        try {

            logger.info("gRPC Server got request for Id = {}", request.getBookId());

            BookEntity entity = bookRepo.findById(request.getBookId())
                    .orElseThrow(() -> new UsernameNotFoundException("User id "+request.getBookId()+" not found"));

            BookResponse response = bookMapper.toBookResponse(entity);

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (UsernameNotFoundException e) {

            responseObserver.onError(
                    Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException()

            );
        } catch (Exception e) {

            logger.error("Unexpected error while processing getBookById", e);
            responseObserver.onError(
                    Status.INTERNAL.withDescription("Internal server error").asRuntimeException()

            );
        }

    }




//    rpc GetAllBooks (EmptyRequest) returns (BookListResponse);
    @Override
    public void getAllBooks(EmptyRequest request, StreamObserver<BookListResponse> responseObserver){

        logger.info("Extracting all books info");

        List<BookEntity> entities = bookCriteria.getAvailBooks();
        logger.info("total books found {}",entities.size());
        BookListResponse listResponse = BookListResponse.newBuilder()
                .addAllBooks(entities.stream().map(bookMapper::toBookResponse).toList())
                .build();

        responseObserver.onNext(listResponse);
        responseObserver.onCompleted();


    }
    
    
    @Override
    public void issueBook(IssueBookRequest bookRequest, StreamObserver<EmptyRequest> responseObserver){
        logger.info("updating issued books");

        List<String> bookIds = bookRequest.getBookIdList().stream().toList();
        int a;
        if(bookRequest.getIssueBooks())
            a=1;
        else {
            a = -1;
        }
        List<BookEntity> entities = bookIds.stream().map(bookid -> bookRepo.findById(bookid).orElseThrow()).toList();
        List<BookEntity> updatedEntity = entities.stream()
                .peek(entity -> entity
                        .setAvailableCopies(
                                entity.getAvailableCopies() - a
                        )
                ).toList();
        bookRepo.saveAll(updatedEntity);
        logger.info("total entities {}",updatedEntity.size());

        responseObserver.onNext(EmptyRequest.newBuilder().build());
        responseObserver.onCompleted();


    }

    @Override
    public void addBook(CreateListBook request, StreamObserver<EmptyRequest> responseObserver){

        logger.info("received request for {} books ",request.getBookRequestCount());
        List<CreateBookRequest> bookRequests = request.getBookRequestList();
        List<BookEntity> bookEntities = bookRequests.stream().map(bookMapper::toEntity).toList();
        bookRepo.saveAll(bookEntities);
        responseObserver.onNext(EmptyRequest.newBuilder().build());
        responseObserver.onCompleted();
    }



    
//    rpc AddBook (CreateBookRequest) returns (BookResponse);
//    rpc DeleteBook (BookIdRequest) returns (EmptyRequest);
//    rpc IssueBook (IssueBookRequest) returns (IssueBookResponse);
//    rpc ReturnBook (ReturnBookRequest) returns (ReturnBookResponse);


}
