package com.beside.archivist.exception.images;

import com.beside.archivist.exception.common.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class ImageNotFoundException extends RuntimeException{
    private final ExceptionCode exceptionCode;
}
