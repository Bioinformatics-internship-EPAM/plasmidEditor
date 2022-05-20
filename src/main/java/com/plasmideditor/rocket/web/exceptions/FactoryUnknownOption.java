package com.plasmideditor.rocket.web.exceptions;

public class FactoryUnknownOption extends RuntimeException {
    public FactoryUnknownOption(String msg) {
        super(msg);
    }
    public FactoryUnknownOption(String msg, Throwable e) {
        super(msg, e);
    }
}

