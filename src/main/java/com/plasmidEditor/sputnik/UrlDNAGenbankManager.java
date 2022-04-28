package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.exceptions.*;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.*;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;
import org.springframework.lang.NonNull;

import java.io.IOException;

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
        } catch (InterruptedException | IOException | CompoundNotFoundException e) {
            throw new ReadGenbankUrlException(accession, e);
        }
    }

    @Override
    public void writeSequence(@NonNull String path, DNASequence sequence) {
        throw new UnsupportedWritingToUrlException();
    }
}
