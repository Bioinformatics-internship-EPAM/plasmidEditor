package com.plasmideditor.rocket.exceptions;

public class GenBankFileNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1927124061910845061L;

    public GenBankFileNotFoundException(Long id) {
        super(String.format("GenBank with id '%d' not found", id));
    }

    public GenBankFileNotFoundException(String accession, String version) {
        super(String.format("GenBank with accession '%s' and version '%s' not found", accession, version));
    }

    public GenBankFileNotFoundException(String accession) {
        super(String.format("GenBank with accession '%s' and latest version not found", accession));
    }

}
