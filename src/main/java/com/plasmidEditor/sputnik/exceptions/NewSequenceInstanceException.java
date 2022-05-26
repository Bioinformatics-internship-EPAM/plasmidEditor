package com.plasmidEditor.sputnik.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NewSequenceInstanceException extends RuntimeException {
    public NewSequenceInstanceException(String message, Exception e) {
        super(message, e);
    }
}
