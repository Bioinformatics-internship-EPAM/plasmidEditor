package com.plasmidEditor.sputnik.exceptions;

import lombok.Getter;

import java.io.IOException;

@Getter
public class ReadGenbankFileException extends RuntimeException {
    private final String path;

    public ReadGenbankFileException(String path, Exception e) {
        super(String.format("Unable to read Genbank file %s", path), e);
        this.path = path;
    }
}
