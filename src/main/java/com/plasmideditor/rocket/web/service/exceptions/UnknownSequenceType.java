package com.plasmideditor.rocket.web.service.exceptions;

public class UnknownSequenceType  extends RuntimeException {
    public UnknownSequenceType(String msg) {
        super(msg);
    }
    public UnknownSequenceType(String msg, Throwable e) {
        super(msg, e);
    }
}
