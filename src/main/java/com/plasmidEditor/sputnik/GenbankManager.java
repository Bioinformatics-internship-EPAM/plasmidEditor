package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.exceptions.WriteGenbankFileException;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.springframework.lang.NonNull;

import java.io.IOException;

public interface GenbankManager<T extends AbstractSequence<?>> {
    T readSequence(@NonNull String url) throws IOException;

    void writeSequence(@NonNull String path, T sequence) throws WriteGenbankFileException;
}
