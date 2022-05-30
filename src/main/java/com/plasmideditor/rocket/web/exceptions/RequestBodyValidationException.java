package com.plasmideditor.rocket.web.exceptions;

public class RequestBodyValidationException extends RuntimeException {
    private static final long serialVersionUID = 3860439316724065089L;

    public RequestBodyValidationException(String msg) {
        super(msg);
    }

    public RequestBodyValidationException(String msg, Throwable e) {
        super(msg, e);
    }

}
