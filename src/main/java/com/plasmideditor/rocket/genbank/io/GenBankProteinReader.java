package com.plasmideditor.rocket.genbank.io;

import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GenBankProteinReader implements GenBankReader<ProteinSequence> {

    @Override
    public ProteinSequence readFromURL(String accessionID) {
        ProteinSequence proteinSequence = null;

        try {
            GenbankProxySequenceReader<AminoAcidCompound> genbankProteinReader = new GenbankProxySequenceReader<>(
                    "/tmp",
                    accessionID,
                    AminoAcidCompoundSet.getAminoAcidCompoundSet()
            );
            proteinSequence = new ProteinSequence(genbankProteinReader);
            genbankProteinReader
                    .getHeaderParser()
                    .parseHeader(genbankProteinReader.getHeader(), proteinSequence);
        } catch (IOException | InterruptedException | CompoundNotFoundException e) {
            e.printStackTrace();
        }

        return proteinSequence;
    }

    @Override
    public ArrayList<ProteinSequence> readFromFile(String filename) {
        ArrayList<ProteinSequence> proteinSequencesList = new ArrayList<>();

        try {
            File protFile = new File(filename);

            LinkedHashMap<String, ProteinSequence> protSequences =
                    GenbankReaderHelper.readGenbankProteinSequence(protFile);
            proteinSequencesList.addAll(protSequences.values());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return proteinSequencesList;
    }
}
