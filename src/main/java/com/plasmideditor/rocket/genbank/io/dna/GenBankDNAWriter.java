package com.plasmideditor.rocket.genbank.io;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.GenbankWriterHelper;

import java.io.*;
import java.util.List;

public class GenBankDNAWriter implements GenBankWriter<DNASequence> {

    @Override
    public void writeToFile(List<DNASequence> dnaSequences, String filename) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GenbankWriterHelper.writeNucleotideSequence(byteArrayOutputStream, dnaSequences,
                    GenbankWriterHelper.LINEAR_DNA);

            try(OutputStream outputStream = new FileOutputStream(filename)) {
                byteArrayOutputStream.writeTo(outputStream);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find file " + filename);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error while writing DNA Sequence to file " + filename);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
