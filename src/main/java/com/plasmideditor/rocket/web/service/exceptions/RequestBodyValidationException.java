package com.plasmideditor.rocket.web.service.exceptions;

public class RequestBodyValidationException extends Exception {

    public RequestBodyValidationException(String msg) {
        super(msg);
    }

    public RequestBodyValidationException(String msg, Throwable e) {
        super(msg, e);
    }

}
