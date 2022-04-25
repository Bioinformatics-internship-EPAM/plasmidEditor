package com.plasmideditor.rocket.genbank.io;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.GenbankWriterHelper;

import java.io.*;
import java.util.List;

public class GenBankProteinWriter implements GenBankWriter<ProteinSequence> {

    @Override
    public void writeToFile(List<ProteinSequence> sequences, String filename) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GenbankWriterHelper.writeProteinSequence(byteArrayOutputStream, sequences);

            try(OutputStream outputStream = new FileOutputStream(filename)) {
                byteArrayOutputStream.writeTo(outputStream);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find file " + filename);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error while writing Protein Sequence to file " + filename);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
