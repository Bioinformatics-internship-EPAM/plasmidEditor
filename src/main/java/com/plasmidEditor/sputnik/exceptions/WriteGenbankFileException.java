package com.plasmidEditor.sputnik.exceptions;

import lombok.*;

import java.io.IOException;

@Getter
public class WriteGenbankFileException extends RuntimeException {
    private final String path;

    public WriteGenbankFileException(String path, Exception e) {
        super(String.format("Can't write sequence to GenBank file %s", path), e);
        this.path = path;
    }
}
