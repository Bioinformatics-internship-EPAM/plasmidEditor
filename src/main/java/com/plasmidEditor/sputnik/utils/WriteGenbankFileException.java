package com.plasmidEditor.sputnik.utils;

import lombok.*;

import java.io.IOException;

@Getter(AccessLevel.PUBLIC)
public class WriteGenbankFileException extends IOException {
    private final String path;

    public WriteGenbankFileException(String path, Throwable e) {
        super(String.format("Can't write sequence to GenBank file %s", path), e);
        this.path = path;
    }
}
