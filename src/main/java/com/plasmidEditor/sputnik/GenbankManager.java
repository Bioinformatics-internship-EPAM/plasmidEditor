package com.plasmidEditor.sputnik;

import org.biojava.nbio.core.sequence.template.AbstractSequence;

public interface GenbankManager<T extends AbstractSequence<?>> {
    public T readByURL(String accession);

    public T readFromFile(String path);

    public void writeToFile(String path, T sequence);
}
