package com.plasmidEditor.sputnik.utils;

public class ReadGenbankFileException extends Exception {
    private final String path;

    public ReadGenbankFileException(String path){
        super("Unable to read Genbank file " + path);
        this.path = path;
    }
    public ReadGenbankFileException(String path, Throwable e){
        super("Unable to read Genbank file " + path, e);
        this.path = path;
    }
    public ReadGenbankFileException(String message, String path, Throwable e){
        super("Unable to read Genbank file " + path + message, e);
        this.path = path;
    }
    public String getPath() {
        return path;
    }
}
