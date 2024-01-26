package com.beside.archivist.exception.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/** 예외 코드 관리 **/
@AllArgsConstructor @Getter
public enum ExceptionCode {
    INVALID_FILE_EXTENSION(BAD_REQUEST, "IMAGE_001", "잘못된 확장자입니다."),
    MAX_SIZE_EXCEEDED(EXPECTATION_FAILED, "IMAGE_002", "10MB 이하의 파일로 올려주세요."),

    USER_ALREADY_EXISTS(CONFLICT, "USER_001", "이미 등록된 회원입니다."),
    SIGN_UP_REQUIRED(NOT_FOUND,"USER_002", "등록되지 않은 이메일입니다. 회원가입을 진행해주세요."),
    USER_NOT_FOUND(NOT_FOUND,"USER_003","사용자 정보가 존재하지 않습니다."),
    EMAIL_TOKEN_MISMATCH(FORBIDDEN, "USER_004", "회원과 토큰의 정보가 맞지 않습니다."),
    AUTHORIZATION_CODE_EXPIRED(BAD_REQUEST, "USER_005", "인가 코드가 만료되었습니다. 재발급 받아주세요."),
    MISSING_AUTHENTICATION(UNAUTHORIZED, "USER_006", "요청 헤더에 인증 토큰을 포함시켜 주세요."),
    TOKEN_EXPIRED(UNAUTHORIZED, "USER_007", "토큰이 만료되었습니다. 재발급 받아주세요."),
    INVALID_TOKEN(UNAUTHORIZED, "USER_008","토큰이 잘못되었거나 토큰에 대한 회원 정보가 존재하지 않습니다."),

    INVALID_CATEGORY_NAME(BAD_REQUEST,"CATEGORY_001", "정의되지 않은 카테고리 값 입니다."),
  
    REQUEST_PART_MISSING(BAD_REQUEST,"VALID_001","필수 값이 누락되었습니다."),
  
    LINK_NOT_FOUND(NOT_FOUND,"LINK_001","링크 정보가 존재하지 않습니다."),

    GROUP_NOT_FOUND(NOT_FOUND,"GROUP_001","그룹 정보가 존재하지 않습니다."),
    GROUP_IN_BOOKMARK_NOT_FOUND(NOT_FOUND,"GROUP_002","북마크 내 그룹 정보가 존재하지 않습니다."),
    GROUP_ALREADY_EXISTS(CONFLICT,"GROUP_003", "이미 저장된 그룹입니다."),
    ;

    private final HttpStatus status; // HTTP 상태 코드
    private final String code; // 우리가 정의한 코드
    private final String message; // 응답 메시지
}
