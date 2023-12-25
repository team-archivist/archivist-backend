package com.beside.archivist.dto.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Getter
public class ExceptionDto {
    private int statusCode;
    private String message;
    @Builder
    public ExceptionDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
