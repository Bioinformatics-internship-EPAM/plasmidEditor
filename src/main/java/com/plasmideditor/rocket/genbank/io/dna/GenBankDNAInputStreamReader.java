package com.plasmideditor.rocket.genbank.io.dna;

import com.plasmideditor.rocket.genbank.io.GenBankReader;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankInputStreamReaderException;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankReaderException;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.DNASequenceCreator;
import org.biojava.nbio.core.sequence.io.GenbankReader;
import org.biojava.nbio.core.sequence.io.GenericGenbankHeaderParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GenBankDNAInputStreamReader implements GenBankReader<DNASequence, InputStream> {

    @Override
    public List<DNASequence> readSequence(InputStream inputStream) throws GenBankReaderException {
        GenbankReader<DNASequence, NucleotideCompound> dnaReader =
                new GenbankReader<>(
                        inputStream,
                        new GenericGenbankHeaderParser<>(),
                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet())
                );
        try {
            LinkedHashMap<String, DNASequence> dnaSequences = dnaReader.process();
            return new ArrayList<>(dnaSequences.values());
        } catch (IOException | CompoundNotFoundException exception) {
            throw new GenBankInputStreamReaderException("Failed to read DNA sequence from input stream", exception);
        }
    }
}
