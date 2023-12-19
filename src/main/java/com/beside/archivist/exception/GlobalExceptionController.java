package com.beside.archivist.exception;

import com.beside.archivist.exception.users.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** 전역 예외 처리 **/
@RestControllerAdvice
public class GlobalExceptionController {

    /** USER_001 중복 회원 체크 **/
    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<ExceptionResponse> handlerUserNotFound(UserAlreadyExistsException ex) {
        final ExceptionResponse responseError = ExceptionResponse.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }
}
