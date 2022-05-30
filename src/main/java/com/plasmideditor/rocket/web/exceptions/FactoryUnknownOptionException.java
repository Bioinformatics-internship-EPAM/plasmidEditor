package com.plasmideditor.rocket.web.exceptions;

public class FactoryUnknownOptionException extends RuntimeException {
    private static final long serialVersionUID = -5352309662890006726L;

    public FactoryUnknownOptionException(String msg) {
        super(msg);
    }
    public FactoryUnknownOptionException(String msg, Throwable e) {
        super(msg, e);
    }
}

