package com.plasmidEditor.sputnik.utils;

public class ReadGenbankUrlException extends Exception {
    private final String accession;

    public ReadGenbankUrlException(String accession, Throwable e){
        super("Unable to get Genbank file with accession " + accession + " by URL", e);
        this.accession = accession;
    }

    public String getAccession() {
        return accession;
    }
}
