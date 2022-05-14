package com.plasmideditor.rocket.genbank.io.dna;

import com.plasmideditor.rocket.genbank.io.GenBankWriter;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankWriterException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.GenbankWriterHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class GenBankDNAWriter implements GenBankWriter<DNASequence> {

    @Override
    public void writeToFile(List<DNASequence> dnaSequences, String filename) throws GenBankWriterException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GenbankWriterHelper.writeNucleotideSequence(byteArrayOutputStream, dnaSequences,
                    GenbankWriterHelper.LINEAR_DNA);

            try(OutputStream outputStream = new FileOutputStream(filename)) {
                byteArrayOutputStream.writeTo(outputStream);
            }
        } catch (Exception e) {
            throw new GenBankWriterException("Failed to write DNA data to file " + filename, e);
        }
    }
}
