package com.plasmideditor.rocket.web.exceptions;

public class FileUploadException extends RuntimeException {
    private static final long serialVersionUID = -5711920622233995913L;

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
