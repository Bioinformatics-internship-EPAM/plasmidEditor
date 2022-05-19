package com.plasmideditor.rocket.web.exceptions;

public class RequestBodyValidationException extends RuntimeException {

    public RequestBodyValidationException(String msg) {
        super(msg);
    }

    public RequestBodyValidationException(String msg, Throwable e) {
        super(msg, e);
    }

}
