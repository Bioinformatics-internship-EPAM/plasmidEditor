package com.plasmideditor.rocket.web.service.utils;

import com.plasmideditor.rocket.web.exceptions.FactoryUnknownOption;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;

import static com.plasmideditor.rocket.web.service.utils.CompoundNames.DNA_CLASS;
import static com.plasmideditor.rocket.web.service.utils.CompoundNames.PROTEIN_CLASS;

public class GenbankSequenceParserFactory {
    public static GenbankSequenceParser createGenbankSequenceParser(String compound) {
        if (compound.equals(DNA_CLASS)) {
            return new GenbankSequenceParser<DNASequence, NucleotideCompound>();
        }
        if (compound.equals(PROTEIN_CLASS)) {
            return new GenbankSequenceParser<ProteinSequence, AminoAcidCompound>();
        }
        throw new FactoryUnknownOption("Unknown option in GenbankSequenceParserFactory");
    }
}
