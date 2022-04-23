package com.plasmideditor.rocket.genbank.io;

import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GenBankDNAReader implements GenBankReader<DNASequence> {

    @Override
    public DNASequence readFromURL(String accessionID) {
        DNASequence dnaSequence = null;

        try {
            GenbankProxySequenceReader<NucleotideCompound> genbankDNAReader =
                    new GenbankProxySequenceReader<>(
                            "/tmp",
                            accessionID,
                            DNACompoundSet.getDNACompoundSet()
                    );
            dnaSequence = new DNASequence(genbankDNAReader);
            genbankDNAReader
                    .getHeaderParser()
                    .parseHeader(genbankDNAReader.getHeader(), dnaSequence);
        } catch (IOException | InterruptedException | CompoundNotFoundException e) {
            e.printStackTrace();
        }

        return dnaSequence;
    }

    @Override
    public ArrayList<DNASequence> readFromFile(String filename) {
        ArrayList<DNASequence> dnaSequencesList = new ArrayList<>();

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
