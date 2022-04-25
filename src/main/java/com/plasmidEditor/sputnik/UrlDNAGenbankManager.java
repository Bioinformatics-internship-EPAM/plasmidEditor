package com.plasmidEditor.sputnik;

import org.biojava.nbio.core.sequence.*;
import org.biojava.nbio.core.sequence.compound.*;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;
import org.springframework.lang.NonNull;

public class UrlDNAGenbankManager implements GenbankManager<DNASequence> {
    @Override
    public DNASequence readSequence(@NonNull String accession) {
        try {
            GenbankProxySequenceReader<NucleotideCompound> genbankDNAReader =
                new GenbankProxySequenceReader<>("/tmp", accession,
                    DNACompoundSet.getDNACompoundSet());
            DNASequence dnaSequence = new DNASequence(genbankDNAReader);
            genbankDNAReader.getHeaderParser().parseHeader(genbankDNAReader.getHeader(), dnaSequence);
            return dnaSequence;
        } catch (Exception e) {
            throw new RuntimeException("Can't read sequence from GenBank", e);
        }
    }

    @Override
    public void writeSequence(@NonNull String path, DNASequence sequence) {
        throw new UnsupportedOperationException("Can't write to URL");
    }
}
