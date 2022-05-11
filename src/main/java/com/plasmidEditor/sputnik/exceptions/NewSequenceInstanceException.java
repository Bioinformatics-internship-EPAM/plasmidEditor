package com.plasmidEditor.sputnik.exceptions;

public class NewSequenceInstanceException extends RuntimeException {
    public NewSequenceInstanceException(String message, Exception e) {
        super(message, e);
    }
}
