package com.plasmideditor.rocket.genbank.io;

import java.util.ArrayList;

public interface GenBankWriter<T> {

    public void writeToFile(ArrayList<T> sequences, String filename);
}
