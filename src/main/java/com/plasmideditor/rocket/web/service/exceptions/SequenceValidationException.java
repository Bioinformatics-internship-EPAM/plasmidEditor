package com.plasmideditor.rocket.web.service.exceptions;

public class SequenceValidationException extends Exception {
    private ExpectedType type;

    public SequenceValidationException(String msg, ExpectedType type) {
        super(msg);
        this.type = type;
    }
    public SequenceValidationException(String msg, Throwable e, ExpectedType type) {
        super(msg, e);
        this.type = type;
    }

    public ExpectedType getType() {
        return type;
    }

    public enum ExpectedType {
        DNA,
        PROTEIN
    }
}

