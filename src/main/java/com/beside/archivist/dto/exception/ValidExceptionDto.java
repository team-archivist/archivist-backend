package com.beside.archivist.dto.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor @Getter
public class ValidExceptionDto {
    private int statusCode;
    private Map<String,String> message;
    @Builder
    public ValidExceptionDto(int statusCode, Map<String,String> messages) {
        this.statusCode = statusCode;
        this.message = messages;
    }
}
