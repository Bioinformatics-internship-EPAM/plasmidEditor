package com.plasmideditor.rocket.genbank.io.exceptions;

public abstract class GenBankReaderException extends Exception {
    public GenBankReaderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
