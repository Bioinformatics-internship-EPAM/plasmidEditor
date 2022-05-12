package com.plasmideditor.rocket.web.service.exceptions;

public class SequenceValidationException extends Exception {

    public SequenceValidationException(String msg) {
        super(msg);
    }

    public SequenceValidationException(String msg, Throwable e) {
        super(msg, e);
    }

}

