package com.beside.archivist.exception.common;

import com.beside.archivist.dto.exception.ExceptionDto;
import com.beside.archivist.dto.exception.LoginFailureDto;
import com.beside.archivist.dto.exception.ValidExceptionDto;
import com.beside.archivist.exception.group.GroupAlreadyExistsException;
import com.beside.archivist.exception.group.GroupNotFoundException;
import com.beside.archivist.exception.images.ImageNotFoundException;
import com.beside.archivist.exception.images.InvalidFileExtensionException;
import com.beside.archivist.exception.group.GroupInBookmarkNotFoundException;
import com.beside.archivist.exception.link.LinkInGroupNotFoundException;
import com.beside.archivist.exception.link.LinkNotFoundException;
import com.beside.archivist.exception.users.*;
import io.jsonwebtoken.security.SignatureException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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

    /** IMAGE_001 이미지 확장자 체크 **/
    @ExceptionHandler(InvalidFileExtensionException.class)
    protected ResponseEntity<ExceptionDto> handlerInvalidFileExtensionException(InvalidFileExtensionException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** IMAGE_002 이미지 크기 체크 **/
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ExceptionDto> handleMaxSizeExceededException() {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ExceptionCode.MAX_SIZE_EXCEEDED.getStatus().value())
                .message(ExceptionCode.MAX_SIZE_EXCEEDED.getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** IMAGE_003 이미지 유무 체크 **/
    @ExceptionHandler(ImageNotFoundException.class)
    protected ResponseEntity<ExceptionDto> handlerImageNotFoundException(ImageNotFoundException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
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

    /** USER_002 회원 가입 유무 체크 **/
    @ExceptionHandler(SignUpRequiredException.class)
    protected ResponseEntity<LoginFailureDto> handlerSignUpRequiredException(SignUpRequiredException ex) {
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

    /** USER_003 회원 유무 체크 **/
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ExceptionDto> handlerUserNotFoundException(UserNotFoundException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** USER_004 토큰에서 추출한 이메일과 요청받은 이메일이 맞지 않은 경우 **/
    @ExceptionHandler(EmailTokenMismatchException.class)
    protected ResponseEntity<ExceptionDto> handlerEmailTokenMismatchException(EmailTokenMismatchException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }
  
    /** USER_005 카카오 인가코드가 만료되거나 잘못되었을 경우 체크 **/
    @ExceptionHandler(WebClientResponseException.class)
    protected ResponseEntity<ExceptionDto> handlerWebClientResponseException() {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ExceptionCode.AUTHORIZATION_CODE_EXPIRED.getStatus().value())
                .message(ExceptionCode.AUTHORIZATION_CODE_EXPIRED.getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** USER_006 토큰 인증 시 요청 헤더에 토큰이 없는 경우 **/
    @ExceptionHandler(MissingAuthenticationException.class)
    protected ResponseEntity<ExceptionDto> handlerMissingAuthenticationException(MissingAuthenticationException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** USER_008 JWT 가 잘못된 경우 **/
    @ExceptionHandler(SignatureException.class)
    protected ResponseEntity<ExceptionDto> handlerInvalidTokenException() {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ExceptionCode.INVALID_TOKEN.getStatus().value())
                .message(ExceptionCode.INVALID_TOKEN.getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** CATEGORY_001 정의되지 않은 카테고리 값 체크 **/
    @ExceptionHandler(InvalidCategoryNameException.class)
    protected ResponseEntity<ExceptionDto> handlerInvalidCategoryNameException(InvalidCategoryNameException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }
  
    /** VALID_001 필수 값 체크 **/
    @ExceptionHandler(MissingServletRequestPartException.class)
    protected ResponseEntity<ExceptionDto> handlerRequestPartMissingException(MissingServletRequestPartException e) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ExceptionCode.REQUEST_PART_MISSING.getStatus().value())
                .message(ExceptionCode.REQUEST_PART_MISSING.getMessageWithParameter(e.getRequestPartName()))
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** LINK_001 링크 유무 체크 **/
    @ExceptionHandler(LinkNotFoundException.class)
    protected ResponseEntity<ExceptionDto> handlerLinkNotFoundException(LinkNotFoundException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** LINK_002 그룹 내 링크 유무 체크 **/
    @ExceptionHandler(LinkInGroupNotFoundException.class)
    protected ResponseEntity<ExceptionDto> handlerLinkInGroupNotFoundException(LinkInGroupNotFoundException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** GROUP_001 그룹 유무 체크 **/
    @ExceptionHandler(GroupNotFoundException.class)
    protected ResponseEntity<ExceptionDto> handlerGroupNotFoundException(GroupNotFoundException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** GROUP_002 북마크 그룹 내 그룹 유무 체크 **/
    @ExceptionHandler(GroupInBookmarkNotFoundException.class)
    protected ResponseEntity<ExceptionDto> handlerLinkInGroupNotFoundException(GroupInBookmarkNotFoundException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }

    /** GROUP_003 중복 그룹 유무 체크 **/
    @ExceptionHandler(GroupAlreadyExistsException.class)
    protected ResponseEntity<ExceptionDto> handlerGroupAlreadyExistsException(GroupAlreadyExistsException ex) {
        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(ex.getExceptionCode().getStatus().value())
                .message(ex.getExceptionCode().getMessage())
                .build();
        return ResponseEntity.status(responseError.getStatusCode()).body(responseError);
    }
}
