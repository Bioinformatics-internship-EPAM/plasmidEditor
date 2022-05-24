package com.plasmidEditor.sputnik.exceptions;

import lombok.Getter;

@Getter
public class ReadGenbankUrlException extends RuntimeException {
    private static final long serialVersionUID = 1003L;
    private final String accession;

    public ReadGenbankUrlException(String accession, Exception e) {
        super(String.format("Unable to get Genbank file with accession %s by URL", accession), e);
        this.accession = accession;
    }
}
