package com.plasmideditor.rocket.genbank.io;

import java.util.ArrayList;

public interface GenBankReader<T> {

    /* Reads file from https://www.ncbi.nlm.nih.gov */
    public T readFromURL(String accessionID);

    public ArrayList<T> readFromFile(String filename);
}
