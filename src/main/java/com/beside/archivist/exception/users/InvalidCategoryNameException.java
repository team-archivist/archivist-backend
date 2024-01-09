package com.beside.archivist.exception.users;

import com.beside.archivist.exception.common.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class InvalidCategoryNameException extends RuntimeException{
    private final ExceptionCode exceptionCode;
}
