package com.beside.archivist.exception.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/** 예외 코드 관리 **/
@AllArgsConstructor @Getter
public enum ExceptionCode {
    INVALID_FILE_EXTENSION(BAD_REQUEST, "IMAGE_001", "잘못된 확장자입니다."),
    USER_ALREADY_EXISTS(CONFLICT, "USER_001", "이미 등록된 회원입니다."),
    USER_NOT_FOUND(NOT_FOUND,"USER_02", "등록되지 않은 회원입니다.");

    private final HttpStatus status; // HTTP 상태 코드
    private final String code; // 우리가 정의한 코드
    private final String message; // 응답 메시지
}
