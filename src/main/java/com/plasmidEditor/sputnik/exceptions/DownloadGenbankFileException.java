package com.plasmidEditor.sputnik.exceptions;

import lombok.Getter;

@Getter
public class DownloadGenbankFileException extends RuntimeException {

    public DownloadGenbankFileException(String accession) {
        super(String.format("GenBank with accession '%s' not exists", accession));
    }

    public DownloadGenbankFileException(String accession, String version) {
        super(String.format("GenBank with accession '%s' version '%s' not exists", accession, version));
    }


}
