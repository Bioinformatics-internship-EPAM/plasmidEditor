package com.plasmideditor.rocket.web.configuration;

import com.plasmideditor.rocket.web.response.ErrorResponse;
import com.plasmideditor.rocket.web.service.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RocketExceptionHandler {

    @ExceptionHandler(UnknownSequenceType.class)
    public ResponseEntity<ErrorResponse> handleUnknownSequenceException(UnknownSequenceType e) {
        String response = "Sequence type is unknown";
        return new ResponseEntity<>(new ErrorResponse(response), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SequenceValidationException.class)
    public ResponseEntity<ErrorResponse> handleSequenceValidationException(SequenceValidationException e) {
        String response = "Sequence not suitable for file: " + e.getMessage();
        return new ResponseEntity<>(new ErrorResponse(response), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenBankFileEditorException.class)
    public ResponseEntity<ErrorResponse> handleUnknownSequenceException(GenBankFileEditorException e) {
        String response = "Internal modification error: " + e.getMessage();
        return new ResponseEntity<>(new ErrorResponse(response), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GenBankFileNotFound.class)
    public ResponseEntity<ErrorResponse> handleSGenBankFileNotFound(GenBankFileNotFound e) {
        String response = "Requested file does not exist in database";
        return new ResponseEntity<>(new ErrorResponse(response), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestBodyValidationException.class)
    public ResponseEntity<ErrorResponse> handleSGenBankFileNotFound(RequestBodyValidationException e) {
        String response = e.getMessage();
        return new ResponseEntity<>(new ErrorResponse(response), HttpStatus.BAD_REQUEST);
    }

}