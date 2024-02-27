package com.beside.archivist.exception.link;

import com.beside.archivist.exception.common.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LinkInGroupNotFoundException extends RuntimeException{
    private final ExceptionCode exceptionCode;
}
