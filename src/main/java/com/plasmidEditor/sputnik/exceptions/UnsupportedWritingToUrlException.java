package com.plasmidEditor.sputnik.exceptions;

public class UnsupportedWritingToUrlException extends UnsupportedOperationException {
    public UnsupportedWritingToUrlException() {
        super("Can't write to URL");
    }
}
