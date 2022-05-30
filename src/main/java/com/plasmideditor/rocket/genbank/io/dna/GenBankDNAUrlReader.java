package com.plasmideditor.rocket.genbank.io.dna;

import com.plasmideditor.rocket.genbank.io.GenBankReader;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankUrlReaderException;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GenBankDNAUrlReader implements GenBankReader<DNASequence, String> {

    @Override
    public List<DNASequence> readSequence(@NonNull String accessionID) throws GenBankUrlReaderException {
        try {
            String genbankDirectoryCache = "/tmp";
            GenbankProxySequenceReader<NucleotideCompound> genbankDNAReader =
                    new GenbankProxySequenceReader<>(
                            genbankDirectoryCache,
                            accessionID,
                            DNACompoundSet.getDNACompoundSet()
                    );
            DNASequence dnaSequence = new DNASequence(genbankDNAReader);
            genbankDNAReader
                    .getHeaderParser()
                    .parseHeader(genbankDNAReader.getHeader(), dnaSequence);
            return Collections.singletonList(dnaSequence);
        } catch (IOException | InterruptedException | CompoundNotFoundException e) {
            throw new GenBankUrlReaderException(
                    "Failed to read GenBank DNA data from https://www.ncbi.nlm.nih.gov/nuccore" + accessionID,
                    e
            );
        }
    }
}
