package com.plasmidEditor.sputnik.exceptions;

public class GenBankNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1001L;

    public GenBankNotFoundException(Long id) {
        super(String.format("GenBank with id '%d' not found", id));
    }

    public GenBankNotFoundException(String accession, String version) {
        super(String.format("GenBank with accession '%s' version '%s' not found", accession, version));
    }

    public GenBankNotFoundException(String accession) {
        super(String.format("GenBank with accession '%s' not found", accession));
    }
}