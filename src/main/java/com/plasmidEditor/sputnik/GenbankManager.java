package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.utils.ReadGenbankFileException;
import com.plasmidEditor.sputnik.utils.ReadGenbankUrlException;
import com.plasmidEditor.sputnik.utils.WriteGenbankFileException;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.springframework.lang.NonNull;

public interface GenbankManager<T extends AbstractSequence<?>> {
    T readSequence(@NonNull String url) throws ReadGenbankUrlException, ReadGenbankFileException;

    void writeSequence(@NonNull String path, T sequence) throws WriteGenbankFileException,
                                                                UnsupportedOperationException;
}
