package com.plasmidEditor.sputnik;

import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.springframework.lang.NonNull;

public interface GenbankManager<T extends AbstractSequence<?>> {
    public T readByURL(@NonNull String accession);

    public T readFromFile(@NonNull String path);

    public void writeToFile(@NonNull String path, T sequence);
}
