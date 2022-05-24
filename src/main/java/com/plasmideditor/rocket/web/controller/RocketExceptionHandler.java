package com.plasmideditor.rocket.web.controller;

import com.plasmideditor.rocket.web.exceptions.*;
import com.plasmideditor.rocket.web.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class RocketExceptionHandler {

    @ExceptionHandler(UnknownSequenceTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnknownSequenceException(UnknownSequenceTypeException e) {
        String response = "Sequence type is unknown";
        return new ErrorResponse(response);
    }

    @ExceptionHandler(SequenceValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSequenceValidationException(SequenceValidationException e) {
        String response = "Sequence not suitable for file: " + e.getMessage();
        return new ErrorResponse(response);
    }

    @ExceptionHandler(GenBankFileEditorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnknownSequenceException(GenBankFileEditorException e) {
        String response = "Internal modification error: " + e.getMessage();
        return new ErrorResponse(response);
    }

    @ExceptionHandler(GenBankFileNotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSGenBankFileNotFound(GenBankFileNotFound e) {
        String response = "Requested file does not exist in database";
        return new ErrorResponse(response);
    }

    @ExceptionHandler(RequestBodyValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSGenBankFileNotFound(RequestBodyValidationException e) {
        String response = e.getMessage();
        return new ErrorResponse(response);
    }

    @ExceptionHandler(FactoryUnknownOption.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFactoryUnknownOption(RequestBodyValidationException e) {
        String response = e.getMessage();
        return new ErrorResponse(response);
    }

}
