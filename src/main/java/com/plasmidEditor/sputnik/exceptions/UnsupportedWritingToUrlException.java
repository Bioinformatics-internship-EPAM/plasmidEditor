package com.plasmidEditor.sputnik.exceptions;

public class UnsupportedWritingToUrlException extends UnsupportedOperationException {
    private static final long serialVersionUID = 1004L;
    public UnsupportedWritingToUrlException() {
        super("Can't write to URL");
    }
}
