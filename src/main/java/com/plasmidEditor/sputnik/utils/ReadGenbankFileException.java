package com.plasmidEditor.sputnik.utils;

import java.io.IOException;

public class ReadGenbankFileException extends IOException {
    private final String path;

    public ReadGenbankFileException(String path, Throwable e){
        super("Unable to read Genbank file " + path, e);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
