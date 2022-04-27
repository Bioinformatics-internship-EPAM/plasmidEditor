package com.plasmidEditor.sputnik.exceptions;

import lombok.Getter;

import java.io.IOException;

@Getter
public class FileReadingException extends IOException {
    private final String path;

    public FileReadingException(String path, Throwable e) {
        super(String.format("Unable to read string from file %s", path), e);
        this.path = path;
    }
}
