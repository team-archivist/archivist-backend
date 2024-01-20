package com.beside.archivist.exception.group;

import com.beside.archivist.exception.common.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class GroupAlreadyExistsException extends RuntimeException{
    private final ExceptionCode exceptionCode;
}
