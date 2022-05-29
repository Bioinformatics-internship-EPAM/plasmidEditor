package com.plasmidEditor.sputnik.exceptions;


public class BufferedReaderCloseException extends RuntimeException {
    private static final long serialVersionUID = 1011L;
    public BufferedReaderCloseException()  {
        super("Can't close the buffer");
    }
}
