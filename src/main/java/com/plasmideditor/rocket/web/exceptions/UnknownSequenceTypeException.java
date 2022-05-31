package com.plasmideditor.rocket.web.exceptions;

public class UnknownSequenceTypeException extends RuntimeException {
    private static final long serialVersionUID = -7873642483275234428L;

    public UnknownSequenceTypeException(String msg) {
        super(msg);
    }
    public UnknownSequenceTypeException(String msg, Throwable e) {
        super(msg, e);
    }
}
