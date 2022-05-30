package com.plasmideditor.rocket.web.exceptions;

public class SequenceValidationException extends RuntimeException {
    private static final long serialVersionUID = 745954836132150799L;

    public SequenceValidationException(String msg) {
        super(msg);
    }

    public SequenceValidationException(String msg, Throwable e) {
        super(msg, e);
    }

}

