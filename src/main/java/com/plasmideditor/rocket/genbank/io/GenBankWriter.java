package com.plasmideditor.rocket.genbank.io;

import com.plasmideditor.rocket.genbank.io.exceptions.GenBankWriterException;

import java.util.List;

public interface GenBankWriter<T> {

    public void writeToFile(List<T> sequences, String filename) throws GenBankWriterException;
}
