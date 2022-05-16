package com.plasmidEditor.sputnik.editor;

import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

public class ModifyMethod extends EditorMethod {
    @Override
    protected String getEditedSequence(String sequence, EditorParameters parameters) {
        StringBuilder sequenceBuilder = new StringBuilder(sequence);
        sequenceBuilder.replace(
                parameters.getPosition() - 1,
                parameters.getPosition() - 1 + parameters.getSubsequence().length(),
                parameters.getSubsequence());
        return sequenceBuilder.toString();
    }

    @Override
    protected <S extends AbstractSequence<C>, C extends Compound> void adjustLocations
            (S sequence, int position, int offset) {}
}
