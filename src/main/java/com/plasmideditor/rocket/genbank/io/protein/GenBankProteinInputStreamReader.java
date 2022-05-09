package com.plasmideditor.rocket.genbank.io.protein;

import com.plasmideditor.rocket.genbank.io.GenBankReader;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankInputStreamReaderException;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankReaderException;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.io.GenbankReader;
import org.biojava.nbio.core.sequence.io.GenericGenbankHeaderParser;
import org.biojava.nbio.core.sequence.io.ProteinSequenceCreator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GenBankProteinInputStreamReader implements GenBankReader<ProteinSequence, InputStream> {

    @Override
    public List<ProteinSequence> readSequence(InputStream inputStream) throws GenBankReaderException {
        GenbankReader<ProteinSequence, AminoAcidCompound> protReader =
                new GenbankReader<>(
                        inputStream,
                        new GenericGenbankHeaderParser<>(),
                        new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet())
                );
        try {
            LinkedHashMap<String, ProteinSequence> protSequences = protReader.process();
            return new ArrayList<>(protSequences.values());
        } catch (IOException | CompoundNotFoundException exception) {
            throw new GenBankInputStreamReaderException("Failed to read protein sequence from input stream", exception);
        }
    }
}
