package com.plasmidEditor.sputnik;

import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.springframework.lang.NonNull;

import java.io.IOException;

public interface GenbankManager<T extends AbstractSequence<?>> {
    T readSequence(@NonNull String url);

    void writeSequence(@NonNull String path, T sequence);
}
