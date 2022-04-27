package com.plasmidEditor.sputnik.exceptions;

import lombok.Getter;

import java.io.IOException;

@Getter
public class ReadGenbankUrlException extends IOException {
    private final String accession;

    public ReadGenbankUrlException(String accession, Throwable e) {
        super(String.format("Unable to get Genbank file with accession %s by URL", accession), e);
        this.accession = accession;
    }
}
