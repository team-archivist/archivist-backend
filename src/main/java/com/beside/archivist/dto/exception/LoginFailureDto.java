package com.beside.archivist.dto.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginFailureDto{
    private final int statusCode;
    private final String email;
    private final String token;
    @Builder
    public LoginFailureDto(int statusCode, String email, String token) {
        this.statusCode = statusCode;
        this.email = email;
        this.token = token;
    }
}
