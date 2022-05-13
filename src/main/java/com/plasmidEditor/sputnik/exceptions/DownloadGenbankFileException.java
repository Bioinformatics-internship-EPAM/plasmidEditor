package com.plasmidEditor.sputnik.exceptions;

public class DownloadGenbankFileException extends RuntimeException {

    public DownloadGenbankFileException(String accession) {
        super(String.format("GenBank file with accession '%s' not exists", accession));
    }

    public DownloadGenbankFileException(String accession, String version) {
        super(String.format("GenBank file with accession '%s' version '%s' not exists", accession, version));
    }
}
