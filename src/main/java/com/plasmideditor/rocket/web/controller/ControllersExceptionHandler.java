package com.plasmideditor.rocket.web.controller;

import com.plasmideditor.rocket.web.response.ErrorResponse;
import com.plasmideditor.rocket.web.service.exceptions.FileEditorUploadException;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class ControllersExceptionHandler {

    @ExceptionHandler(FileEditorUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileEditorUploadException(FileEditorUploadException e) {
        String msg = "Upload file unexpected error. Reason: " + e.getMessage();
        return new ResponseEntity<>(new ErrorResponse(msg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SequenceValidationException.class)
    public ResponseEntity<ErrorResponse> handleSequenceValidationException(SequenceValidationException e) {
        String msg = "Invalid sequence. Reason: " + e.getMessage();
        return new ResponseEntity<>(new ErrorResponse(msg), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException e) {
        String msg = "Failed to read file from input stream. Reason: " + e.getMessage();
        return new ResponseEntity<>(new ErrorResponse(msg), HttpStatus.BAD_REQUEST);
    }
}
