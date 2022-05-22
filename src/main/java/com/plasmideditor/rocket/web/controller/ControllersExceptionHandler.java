package com.plasmideditor.rocket.web.controller;

import com.plasmideditor.rocket.web.exceptions.FileEditorUploadException;
import com.plasmideditor.rocket.web.exceptions.GenBankFileAlreadyExistsException;
import com.plasmideditor.rocket.web.exceptions.SequenceValidationException;
import com.plasmideditor.rocket.web.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class ControllersExceptionHandler {
    public static final String FILE_EDITOR_UPLOAD_EXCEPTION_MSG = "Upload file unexpected error. Reason: ";
    public static final String SEQUENCE_VALIDATION_EXCEPTION_MSG = "Invalid sequence. Reason: ";
    public static final String IO_EXCEPTION_MSG = "Failed to read file from input stream. Reason: ";

    @ExceptionHandler(FileEditorUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileEditorUploadException(FileEditorUploadException e) {
        String msg = FILE_EDITOR_UPLOAD_EXCEPTION_MSG + e.getMessage();
        return new ResponseEntity<>(new ErrorResponse(msg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SequenceValidationException.class)
    public ResponseEntity<ErrorResponse> handleSequenceValidationException(SequenceValidationException e) {
        String msg = SEQUENCE_VALIDATION_EXCEPTION_MSG + e.getMessage();
        return new ResponseEntity<>(new ErrorResponse(msg), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(GenBankFileAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleGenBankFileAlreadyExistsException(GenBankFileAlreadyExistsException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException e) {
        String msg = IO_EXCEPTION_MSG + e.getMessage();
        return new ResponseEntity<>(new ErrorResponse(msg), HttpStatus.BAD_REQUEST);
    }
}
