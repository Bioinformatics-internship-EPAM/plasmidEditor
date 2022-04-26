package com.plasmidEditor.sputnik.utils;

import lombok.*;

import java.io.IOException;

public class ReadGenbankUrlException extends IOException {
    @Getter(AccessLevel.PUBLIC)
    private final String accession;

    public ReadGenbankUrlException(String accession, Throwable e) {
        super(String.format("Unable to get Genbank file with accession %s by URL", accession), e);
        this.accession = accession;
    }
}
