package com.plasmideditor.rocket.genbank.services.exceptions;

public class GenBankFileEditorException extends Exception {
    public GenBankFileEditorException(String msg) {
        super(msg);
    }
    public GenBankFileEditorException(String msg, Throwable e) {
        super(msg, e);
    }
}
