package com.plasmideditor.rocket.web.exceptions;

public class FileAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = -473826353865576762L;

    public FileAlreadyExistsException(String msg) {
        super(msg);
    }
}
