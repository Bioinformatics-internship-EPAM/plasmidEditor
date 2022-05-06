package com.plasmideditor.rocket.exceptions;

public class GenBankFileNotFoundException extends RuntimeException{

    public GenBankFileNotFoundException(Long id) {
        super(String.format("GenBank with id '%d' not found", id));
    }

    public GenBankFileNotFoundException(String accession, String version) {
        super(String.format("GenBank with accession '%s' and version '%s' not found", accession, version));
    }

}
