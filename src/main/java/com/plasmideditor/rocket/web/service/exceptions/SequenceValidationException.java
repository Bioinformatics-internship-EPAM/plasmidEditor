package com.plasmideditor.rocket.web.service.exceptions;

public class SequenceValidationException extends Exception {
    public SequenceValidationException(String msg) {
        super(msg);
    }

    public SequenceValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
