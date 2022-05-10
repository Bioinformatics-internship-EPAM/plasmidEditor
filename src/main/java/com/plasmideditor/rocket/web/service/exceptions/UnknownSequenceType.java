package com.plasmideditor.rocket.web.service.exceptions;

public class UnknownSequenceType  extends Exception {
    public UnknownSequenceType(String msg) {
        super(msg);
    }
    public UnknownSequenceType(String msg, Throwable e) {
        super(msg, e);
    }
}
