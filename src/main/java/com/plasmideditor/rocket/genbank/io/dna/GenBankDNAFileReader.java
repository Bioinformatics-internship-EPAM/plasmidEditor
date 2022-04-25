package com.plasmideditor.rocket.genbank.io.dna;

import com.plasmideditor.rocket.genbank.io.GenBankReader;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GenBankDNAFileReader implements GenBankReader<DNASequence> {

    @Override
    public List<DNASequence> read_sequence(String filename) {
        List<DNASequence> dnaSequencesList = new ArrayList<>();

        try {
            File dnaFile = new File(filename);

            LinkedHashMap<String, DNASequence> dnaSequences =
                    GenbankReaderHelper.readGenbankDNASequence(dnaFile);
            dnaSequencesList.addAll(dnaSequences.values());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dnaSequencesList;
    }
}
