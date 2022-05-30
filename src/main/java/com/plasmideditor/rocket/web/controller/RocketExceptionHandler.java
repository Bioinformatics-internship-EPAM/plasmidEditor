package com.plasmideditor.rocket.web.controller;

import com.plasmideditor.rocket.web.exceptions.*;
import com.plasmideditor.rocket.web.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ControllerAdvice
@ResponseBody
public class RocketExceptionHandler {
    public static final String FILE_EDITOR_UPLOAD_EXCEPTION_MSG = "Upload file unexpected error. Reason: ";
    public static final String SEQUENCE_VALIDATION_EXCEPTION_MSG = "Sequence not suitable for file: ";
    public static final String IO_EXCEPTION_MSG = "Failed to read file from input stream. Reason: ";

    @ExceptionHandler(UnknownSequenceTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnknownSequenceException(UnknownSequenceTypeException e) {
        String response = "Sequence type is unknown";
        return new ErrorResponse(response);
    }

    @ExceptionHandler(SequenceValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleSequenceValidationException(SequenceValidationException e) {
        String response = SEQUENCE_VALIDATION_EXCEPTION_MSG + e.getMessage();
        return new ErrorResponse(response);
    }

    @ExceptionHandler(GenBankFileEditorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnknownSequenceException(GenBankFileEditorException e) {
        String response = "Internal modification error: " + e.getMessage();
        return new ErrorResponse(response);
    }

    @ExceptionHandler(GenBankFileNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSGenBankFileNotFound(GenBankFileNotFoundException e) {
        String response = "Requested file does not exist in database";
        return new ErrorResponse(response);
    }

    @ExceptionHandler(RequestBodyValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSGenBankFileNotFound(RequestBodyValidationException e) {
        String response = e.getMessage();
        return new ErrorResponse(response);
    }

    @ExceptionHandler(FactoryUnknownOptionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFactoryUnknownOption(RequestBodyValidationException e) {
        String response = e.getMessage();
        return new ErrorResponse(response);
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFileEditorUploadException(FileUploadException e) {
        String msg = FILE_EDITOR_UPLOAD_EXCEPTION_MSG + e.getMessage();
        return new ErrorResponse(msg);
    }

    @ExceptionHandler(FileAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleGenBankFileAlreadyExistsException(FileAlreadyExistsException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIOException(IOException e) {
        String msg = IO_EXCEPTION_MSG + e.getMessage();
        return new ErrorResponse(msg);
    }
}
