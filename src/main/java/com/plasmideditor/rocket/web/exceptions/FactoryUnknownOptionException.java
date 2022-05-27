package com.plasmideditor.rocket.web.exceptions;

public class FactoryUnknownOptionException extends RuntimeException {
    public FactoryUnknownOptionException(String msg) {
        super(msg);
    }
    public FactoryUnknownOptionException(String msg, Throwable e) {
        super(msg, e);
    }
}

