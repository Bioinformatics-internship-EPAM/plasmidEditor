package com.plasmidEditor.sputnik.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DownloadGenbankFileException.class)
    protected ResponseEntity<DownloadGenbankFileJsonException> handleDownloadGenbankFileException(DownloadGenbankFileException e) {
        return new ResponseEntity<>(
                new DownloadGenbankFileJsonException("Fail to download file", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}
