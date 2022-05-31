package com.plasmideditor.rocket.web.exceptions;

public class GenBankFileEditorException extends RuntimeException {
    private static final long serialVersionUID = 5768230143950941908L;

    public GenBankFileEditorException(String msg) {
        super(msg);
    }
    public GenBankFileEditorException(String msg, Throwable e) {
        super(msg, e);
    }
}
