package com.plasmidEditor.sputnik.exceptions;

import lombok.Getter;

import java.io.IOException;

@Getter
public class FileReadingException extends RuntimeException {
    private final String path;

    public FileReadingException(String path, Exception e) {
        super(String.format("Unable to read string from file %s", path), e);
        this.path = path;
    }
}
