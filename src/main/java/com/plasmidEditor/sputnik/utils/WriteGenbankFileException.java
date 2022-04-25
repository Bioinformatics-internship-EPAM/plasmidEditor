package com.plasmidEditor.sputnik.utils;

public class WriteGenbankFileException extends Exception {
    private final String path;

    public WriteGenbankFileException(String path, Throwable e){
        super("Can't write sequence to GenBank file " + path, e);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
