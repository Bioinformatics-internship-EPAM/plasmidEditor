package com.plasmidEditor.sputnik.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ValidationParametersException extends RuntimeException {
    public ValidationParametersException(String message) {
        super(message);
    }
}
