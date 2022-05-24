package com.plasmideditor.rocket.web.exceptions;

public class UnknownSequenceTypeException extends RuntimeException {
    public UnknownSequenceTypeException(String msg) {
        super(msg);
    }
    public UnknownSequenceTypeException(String msg, Throwable e) {
        super(msg, e);
    }
}
