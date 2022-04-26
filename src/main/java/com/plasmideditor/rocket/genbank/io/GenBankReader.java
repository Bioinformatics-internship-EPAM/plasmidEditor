package com.plasmideditor.rocket.genbank.io;

import com.plasmideditor.rocket.genbank.io.exceptions.GenBankReaderException;
import org.springframework.lang.NonNull;

import java.util.List;

public interface GenBankReader<T> {

    public List<T> read_sequence(@NonNull String filename) throws GenBankReaderException;
}
