package com.plasmideditor.rocket.genbank.domains;

import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.AccessionID;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.Strand;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.location.template.AbstractLocation;

import java.util.ArrayList;

public class RaketaDNASequence extends DNASequence {
    private DNASequence parentSequence;
    private String sequence;
    private AbstractLocation location;

    public RaketaDNASequence(DNASequence parentSequence, String sequence) {
        setCompoundSet(DNACompoundSet.getDNACompoundSet());
        try {
            initSequenceStorage(parentSequence.getSequenceAsString());
        } catch (CompoundNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        this.parentSequence = parentSequence;
        this.sequence = sequence;
        setParentSequence(parentSequence);
    }

    @Override
    public int getLength() {
        return sequence.length();
    }

    @Override
    public AccessionID getAccession() {
        return parentSequence.getAccession();
    }

    @Override
    public String getDescription() {
        return parentSequence.getDescription();
    }

    @Override
    public String getSource() {
        return parentSequence.getSource();
    }

    @Override
    public ArrayList<String> getNotesList() {
        return parentSequence.getNotesList();
    }

    @Override
    public String getSequenceAsString(Integer bioStart, Integer bioEnd, Strand strand) {
        return this.sequence.substring(bioStart, bioEnd);
    }

    @Override
    public String getSequenceAsString() {
        return this.sequence;
    }
}
