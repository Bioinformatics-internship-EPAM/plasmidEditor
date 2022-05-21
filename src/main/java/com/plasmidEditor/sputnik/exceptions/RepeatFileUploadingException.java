package com.plasmidEditor.sputnik.exceptions;

public class RepeatFileUploadingException extends RuntimeException {
    public RepeatFileUploadingException(String accession, String version) {
        super(String.format("File with accession %s and version %s already exists", accession, version));
    }
}
