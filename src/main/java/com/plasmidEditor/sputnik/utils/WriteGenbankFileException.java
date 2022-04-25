package com.plasmidEditor.sputnik.utils;

import java.io.IOException;

public class WriteGenbankFileException extends IOException {
    private final String path;

    public WriteGenbankFileException(String path, Throwable e){
        super("Can't write sequence to GenBank file " + path, e);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
