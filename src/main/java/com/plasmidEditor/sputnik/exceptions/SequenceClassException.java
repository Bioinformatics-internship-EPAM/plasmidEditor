package com.plasmidEditor.sputnik.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SequenceClassException extends RuntimeException {
    public SequenceClassException(String message) {
        super(message);
    }
}
