package com.plasmideditor.rocket.genbank.io.exceptions;

public abstract class GenBankReaderException extends Exception {
    private static final long serialVersionUID = 4124821831947373640L;

    public GenBankReaderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
