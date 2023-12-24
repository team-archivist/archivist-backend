package com.beside.archivist.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/** 예외 코드 관리 **/
@AllArgsConstructor @Getter
public enum ExceptionCode {
    USER_ALREADY_EXISTS(CONFLICT, "USER_001", "이미 등록된 회원입니다."),
    USER_NOT_FOUND(NOT_FOUND,"USER_02", "등록되지 않은 회원입니다.");

    private final HttpStatus status; // HTTP 상태 코드
    private final String code; // 우리가 정의한 코드
    private final String message; // 응답 메시지
}
