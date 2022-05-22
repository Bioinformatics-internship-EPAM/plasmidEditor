package com.plasmideditor.rocket.web.exceptions;

public class FileUploadException extends Exception {
    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
