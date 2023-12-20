package com.beside.archivist.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/** 예외 코드 관리 **/
@Getter
public enum ExceptionCode {
    USER_ALREADY_EXISTS(CONFLICT, "USER_001", "이미 등록된 회원입니다.");

    private final HttpStatus status; // HTTP 상태 코드
    private final String code; // 우리가 정의한 코드
    private final String message; // 응답 메시지

    ExceptionCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
