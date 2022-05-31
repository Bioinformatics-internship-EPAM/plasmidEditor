package com.plasmidEditor.sputnik.editor;

import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

public class ModifyMethod extends EditorMethod {
    @Override
    protected String getEditedSequence(String sequence, String subsequence, int position, int cutSize) {
        StringBuilder sequenceBuilder = new StringBuilder(sequence);
        sequenceBuilder.replace(
                position - 1,
                position - 1 + subsequence.length(),
                subsequence);
        return sequenceBuilder.toString();
    }

    @Override
    protected <S extends AbstractSequence<C>, C extends Compound> void adjustLocations
            (S sequence, int position, int offset) {}
}
