package com.plasmideditor.rocket.web.exceptions;

public class FailedRequestHandleException extends RuntimeException {
    private static final long serialVersionUID = 4656715939147792688L;

    public FailedRequestHandleException(String msg) {
        super(msg);
    }

    public FailedRequestHandleException(String msg, Throwable e) {
        super(msg, e);
    }

}
