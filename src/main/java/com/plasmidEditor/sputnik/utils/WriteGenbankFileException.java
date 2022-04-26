package com.plasmidEditor.sputnik.utils;

import lombok.*;

import java.io.IOException;

public class WriteGenbankFileException extends IOException {
    @Getter(AccessLevel.PUBLIC)
    private final String path;

    public WriteGenbankFileException(String path, Throwable e) {
        super(String.format("Can't write sequence to GenBank file %s", path), e);
        this.path = path;
    }
}
