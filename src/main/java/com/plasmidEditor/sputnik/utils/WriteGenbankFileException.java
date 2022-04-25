package com.plasmidEditor.sputnik.utils;

public class WriteGenbankFileException extends Exception {
    private final String path;

    public WriteGenbankFileException(String path){
        super("Can't write sequence to GenBank file " + path);
        this.path = path;
    }
    public WriteGenbankFileException(String path, Throwable e){
        super("Can't write sequence to GenBank file " + path, e);
        this.path = path;
    }
    public WriteGenbankFileException(String message, String path, Throwable e){
        super("Can't write sequence to GenBank file " + path + message, e);
        this.path = path;
    }
    public String getPath() {
        return path;
    }
}
