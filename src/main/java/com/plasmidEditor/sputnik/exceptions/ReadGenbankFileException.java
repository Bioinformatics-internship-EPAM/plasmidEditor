package com.plasmidEditor.sputnik.exceptions;

public class ReadGenbankFileException extends RuntimeException {
    public ReadGenbankFileException(String path, Exception e) {
        super(String.format("Unable to read Genbank file %s", path), e);
    }

    public ReadGenbankFileException() {
        super("Can't read sequence from file");
    }
}
