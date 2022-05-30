package com.plasmideditor.rocket.genbank.io.protein;

import com.plasmideditor.rocket.genbank.io.GenBankWriter;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankWriterException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.GenbankWriterHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class GenBankProteinWriter implements GenBankWriter<ProteinSequence> {

    @Override
    public void writeToFile(List<ProteinSequence> sequences, String filename) throws GenBankWriterException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GenbankWriterHelper.writeProteinSequence(byteArrayOutputStream, sequences);

            try(OutputStream outputStream = new FileOutputStream(filename)) {
                byteArrayOutputStream.writeTo(outputStream);
            }
        } catch (Exception e) {
            throw new GenBankWriterException("Failed to write Protein data to file " + filename, e);
        }
    }
}
