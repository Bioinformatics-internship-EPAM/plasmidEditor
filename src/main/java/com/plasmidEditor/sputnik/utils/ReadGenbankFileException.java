package com.plasmidEditor.sputnik.utils;

import lombok.*;

import java.io.IOException;

public class ReadGenbankFileException extends IOException {
    @Getter(AccessLevel.PUBLIC)
    private final String path;

    public ReadGenbankFileException(String path, Throwable e){
        super(String.format("Unable to read Genbank file %s", path), e);
        this.path = path;
    }
}
