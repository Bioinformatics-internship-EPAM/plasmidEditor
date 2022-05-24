package com.plasmidEditor.sputnik.exceptions;

import lombok.Getter;

@Getter
public class WriteGenbankFileException extends RuntimeException {
    private static final long serialVersionUID = 1005L;
    private final String path;

    public WriteGenbankFileException(String path, Exception e) {
        super(String.format("Can't write sequence to GenBank file %s", path), e);
        this.path = path;
    }
}
