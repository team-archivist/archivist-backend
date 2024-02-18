package com.beside.archivist.exception.images;

import com.beside.archivist.exception.common.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class InvalidFileExtensionException extends RuntimeException {
    private ExceptionCode exceptionCode;
}
