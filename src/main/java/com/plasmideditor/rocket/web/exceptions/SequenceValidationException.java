package com.plasmideditor.rocket.web.exceptions;

public class SequenceValidationException extends RuntimeException {

    public SequenceValidationException(String msg) {
        super(msg);
    }

    public SequenceValidationException(String msg, Throwable e) {
        super(msg, e);
    }

}

