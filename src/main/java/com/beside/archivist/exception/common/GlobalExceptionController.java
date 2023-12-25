package com.beside.archivist.exception.common;

import com.beside.archivist.dto.exception.ExceptionDto;
import com.beside.archivist.dto.exception.ValidExceptionDto;
import com.beside.archivist.exception.users.UserAlreadyExistsException;
import com.beside.archivist.exception.users.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/** 전역 예외 처리 **/
@RestControllerAdvice
public class GlobalExceptionController {

    /** Validation 처리 **/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidExceptionDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage()); // FieldName: errorMessage
        });

        final ValidExceptionDto responseError = ValidExceptionDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .messages(errors)
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** USER_001 중복 회원 체크 **/
    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<ExceptionDto> handlerUserAlreadyExistsException(UserAlreadyExistsException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** USER_002 기존 회원 유무 체크 **/
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ExceptionDto> handlerUserNotFoundException(UserNotFoundException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getEmail())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }
}
