package com.plasmidEditor.sputnik.exceptions;

public class GenBankNotFoundException extends RuntimeException {
    public GenBankNotFoundException(Long id) {
        super(String.format("GenBank with id '%d' not found", id));
    }

    public GenBankNotFoundException(String accession, String version) {
        super(String.format("GenBank with accession '%s' version '%s' not found", accession, version));
    }
}