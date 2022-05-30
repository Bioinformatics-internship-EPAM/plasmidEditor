package com.plasmideditor.rocket.web.exceptions;

public class FileUploadException extends RuntimeException {
    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
