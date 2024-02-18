package com.beside.archivist.exception.users;

import com.beside.archivist.exception.common.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class UserAlreadyExistsException extends RuntimeException{
    private final ExceptionCode exceptionCode;
}
