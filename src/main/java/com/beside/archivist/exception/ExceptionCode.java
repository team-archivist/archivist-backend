package com.beside.archivist.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ExceptionCode {

    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
