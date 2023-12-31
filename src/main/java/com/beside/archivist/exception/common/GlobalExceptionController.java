package com.beside.archivist.exception.common;

import com.beside.archivist.dto.exception.ExceptionDto;
import com.beside.archivist.dto.exception.LoginFailureDto;
import com.beside.archivist.dto.exception.ValidExceptionDto;
import com.beside.archivist.exception.images.InvalidFileExtensionException;
import com.beside.archivist.exception.users.EmailTokenMismatchException;
import com.beside.archivist.exception.users.UserAlreadyExistsException;
import com.beside.archivist.exception.users.UserNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpHeaders;
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

    /** 이미지 확장자 체크 **/
    @ExceptionHandler(InvalidFileExtensionException.class)
    protected ResponseEntity<ExceptionDto> handlerInvalidFileExtensionException(InvalidFileExtensionException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** 이미지 크기 체크 **/
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ExceptionDto> handleMaxSizeExceededException() {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ExceptionCode.MAX_SIZE_EXCEEDED.getStatus().value())
                .message(ExceptionCode.MAX_SIZE_EXCEEDED.getMessage())
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
    protected ResponseEntity<LoginFailureDto> handlerUserNotFoundException(UserNotFoundException ex) {
        final LoginFailureDto responseError = LoginFailureDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .email(ex.getEmail())
                .token(ex.getToken())
                .build();

        // 쿠키에 토큰 정보 추가- FE 요청
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "Token=" + ex.getToken() + "; HttpOnly; Path=/; Max-Age=3600");

        return ResponseEntity.status(responseError.getStatusCode()).headers(headers).body(responseError);
    }

    /** USER_003 토큰에서 추출한 이메일과 요청받은 이메일이 맞지 않은 경우 **/
    @ExceptionHandler(EmailTokenMismatchException.class)
    protected ResponseEntity<ExceptionDto> handlerEmailTokenMismatchException(EmailTokenMismatchException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }
}
