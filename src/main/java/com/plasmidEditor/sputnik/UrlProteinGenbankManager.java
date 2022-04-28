package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.exceptions.*;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.*;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;
import org.springframework.lang.NonNull;

import java.io.IOException;

public class UrlProteinGenbankManager implements GenbankManager<ProteinSequence> {
    @Override
    public ProteinSequence readSequence(@NonNull String accession) {
        try {
            GenbankProxySequenceReader<AminoAcidCompound> genbankProteinReader =
                new GenbankProxySequenceReader<>("/tmp", accession,
                    AminoAcidCompoundSet.getAminoAcidCompoundSet());
            ProteinSequence proteinSequence = new ProteinSequence(genbankProteinReader);
            genbankProteinReader.getHeaderParser().parseHeader(
                genbankProteinReader.getHeader(), proteinSequence);
            return proteinSequence;
        } catch (InterruptedException | IOException | CompoundNotFoundException e) {
            throw new ReadGenbankUrlException(accession, e);
        }
    }

    @Override
    public void writeSequence(@NonNull String path, ProteinSequence sequence) {
        throw new UnsupportedWritingToUrlException();
    }
}
