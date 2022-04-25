package com.plasmidEditor.sputnik;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.*;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;
import org.springframework.lang.NonNull;

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
        } catch (Exception e) {
            throw new RuntimeException("Can't read sequence from GenBank", e);
        }
    }

    @Override
    public void writeSequence(@NonNull String path, ProteinSequence sequence) {
        throw new UnsupportedOperationException("Can't write to URL");
    }
}
