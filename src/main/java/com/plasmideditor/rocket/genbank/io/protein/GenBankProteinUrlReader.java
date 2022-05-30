package com.plasmideditor.rocket.genbank.io.protein;

import com.plasmideditor.rocket.genbank.io.GenBankReader;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankUrlReaderException;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GenBankProteinUrlReader  implements GenBankReader<ProteinSequence, String> {
    private final String genbankDirectoryCache = "/tmp";

    @Override
    public List<ProteinSequence> readSequence(@NonNull String accessionID) throws GenBankUrlReaderException {
        try {
            GenbankProxySequenceReader<AminoAcidCompound> genbankProteinReader = new GenbankProxySequenceReader<>(
                    genbankDirectoryCache,
                    accessionID,
                    AminoAcidCompoundSet.getAminoAcidCompoundSet()
            );
            ProteinSequence proteinSequence = new ProteinSequence(genbankProteinReader);
            genbankProteinReader
                    .getHeaderParser()
                    .parseHeader(genbankProteinReader.getHeader(), proteinSequence);
            return Collections.singletonList(proteinSequence);
        } catch (IOException | InterruptedException | CompoundNotFoundException e) {
            throw new GenBankUrlReaderException(
                    "Failed to read GenBank Protein data from https://www.ncbi.nlm.nih.gov/protein" + accessionID,
                    e
            );
        }
    }
}
