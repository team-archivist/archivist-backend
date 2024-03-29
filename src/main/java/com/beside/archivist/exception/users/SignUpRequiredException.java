package com.beside.archivist.exception.users;

import com.beside.archivist.exception.common.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class SignUpRequiredException extends RuntimeException{
    private final ExceptionCode exceptionCode;
    private final String email;
    private final String token;
}
