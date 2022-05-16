package com.plasmidEditor.sputnik.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DownloadGenbankFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected DownloadGenbankFileErrorMessage handleDownloadGenbankFileException(DownloadGenbankFileException e) {
        return new DownloadGenbankFileErrorMessage("Fail to download file", e.getMessage());
    }
}
