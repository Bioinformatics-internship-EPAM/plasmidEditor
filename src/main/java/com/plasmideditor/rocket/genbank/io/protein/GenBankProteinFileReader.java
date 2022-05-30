package com.plasmideditor.rocket.genbank.io.protein;

import com.plasmideditor.rocket.genbank.io.GenBankReader;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankFileReaderException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.springframework.lang.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GenBankProteinFileReader implements GenBankReader<ProteinSequence, String> {
    @Override
    public List<ProteinSequence> readSequence(@NonNull String filename) throws GenBankFileReaderException {
        try {
            File protFile = new File(filename);

            LinkedHashMap<String, ProteinSequence> protSequences =
                    GenbankReaderHelper.readGenbankProteinSequence(protFile);
            return new ArrayList<>(protSequences.values());
        } catch (Exception e) {
            throw new GenBankFileReaderException("Failed to read Protein data from file " + filename, e);
        }
    }
}
