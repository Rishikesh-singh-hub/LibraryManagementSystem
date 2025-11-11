package com.microservice.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IssueRequestDto {

    @NotEmpty(message = "select a Books to issue book")
    private List<String> bid;


}
