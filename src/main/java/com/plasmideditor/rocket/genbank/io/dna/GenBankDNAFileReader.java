package com.plasmideditor.rocket.genbank.io.dna;

import com.plasmideditor.rocket.genbank.io.GenBankReader;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankFileReaderException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.springframework.lang.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GenBankDNAFileReader implements GenBankReader<DNASequence> {

    @Override
    public List<DNASequence> read_sequence(@NonNull String filename) throws GenBankFileReaderException {
        try {
            File dnaFile = new File(filename);

            LinkedHashMap<String, DNASequence> dnaSequences =
                    GenbankReaderHelper.readGenbankDNASequence(dnaFile);
            List<DNASequence> dnaSequencesList = new ArrayList<>(dnaSequences.values());
            return dnaSequencesList;
        } catch (Exception e) {
            throw new GenBankFileReaderException("Filed to read GenBank DNA data from file " + filename, e);
        }
    }
}
