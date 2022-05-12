package com.plasmideditor.rocket.web.configuration;

import com.plasmideditor.rocket.web.service.exceptions.GenBankFileEditorException;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileNotFound;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import com.plasmideditor.rocket.web.service.exceptions.UnknownSequenceType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RocketExceptionHandler {

    @ExceptionHandler(UnknownSequenceType.class)
    public ResponseEntity<String> handleUnknownSequenceException(UnknownSequenceType e) {
        String response = "Sequence type is unknown";
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SequenceValidationException.class)
    public ResponseEntity<String> handleSequenceValidationException(SequenceValidationException e) {
        String response = "Sequence not suitable for file: " + e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenBankFileEditorException.class)
    public ResponseEntity<String> handleUnknownSequenceException(GenBankFileEditorException e) {
        String response = "Internal modification error: " + e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GenBankFileNotFound.class)
    public ResponseEntity<String> handleSGenBankFileNotFound(GenBankFileNotFound e) {
        String response = "Requested file does not exist in database";
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}