package com.plasmideditor.rocket.genbank.io.dna;

import com.plasmideditor.rocket.genbank.io.GenBankReader;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GenBankDNAUrlReader implements GenBankReader<DNASequence> {
    private final String genbankDirectoryCache = "/tmp";

    @Override
    public List<DNASequence> read_sequence(String accessionID) {
        DNASequence dnaSequence = new DNASequence();

        try {
            GenbankProxySequenceReader<NucleotideCompound> genbankDNAReader =
                    new GenbankProxySequenceReader<>(
                            genbankDirectoryCache,
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

        return Collections.singletonList(dnaSequence);
    }
}
