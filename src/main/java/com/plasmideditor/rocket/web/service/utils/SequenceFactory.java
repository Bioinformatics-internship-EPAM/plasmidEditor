package com.plasmideditor.rocket.web.service.utils;

import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

public class SequenceFactory {
    private static final String PROTEIN_CLASS = "ProteinSequence";
    private static final String DNA_CLASS = "DNASequence";

    public static AbstractSequence create(String type, String sequence) throws CompoundNotFoundException {
        if (type.equals(PROTEIN_CLASS)) {
            return new ProteinSequence(sequence);
        }
        if (type.equals(DNA_CLASS)) {
            return new DNASequence(sequence);
        }
        return null;
    }
}
