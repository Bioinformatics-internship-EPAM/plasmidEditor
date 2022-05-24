package com.plasmideditor.rocket.web.service.utils;

import com.plasmideditor.rocket.web.exceptions.FactoryUnknownOption;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

import static com.plasmideditor.rocket.web.service.utils.CompoundNames.DNA_CLASS;
import static com.plasmideditor.rocket.web.service.utils.CompoundNames.PROTEIN_CLASS;

public class SequenceFactory {

    public static AbstractSequence create(String type, String sequence) throws CompoundNotFoundException {
        switch (type) {
            case PROTEIN_CLASS:
                return new ProteinSequence(sequence);
            case DNA_CLASS:
                return new DNASequence(sequence);
            default:
                throw new FactoryUnknownOption("Unknown option for SequenceFactory");
        }
    }
}
