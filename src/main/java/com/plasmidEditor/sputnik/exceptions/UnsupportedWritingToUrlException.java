package com.plasmidEditor.sputnik.exceptions;

import lombok.Getter;

@Getter
public class UnsupportedWritingToUrlException extends UnsupportedOperationException {
    public UnsupportedWritingToUrlException() {
        super("Can't write to URL");
    }
}
