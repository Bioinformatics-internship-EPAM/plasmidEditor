package com.plasmideditor.rocket.web.service.exceptions;

public class GenBankFileEditorException extends Exception {
    public GenBankFileEditorException(String msg) {
        super(msg);
    }
    public GenBankFileEditorException(String msg, Throwable e) {
        super(msg, e);
    }
}
