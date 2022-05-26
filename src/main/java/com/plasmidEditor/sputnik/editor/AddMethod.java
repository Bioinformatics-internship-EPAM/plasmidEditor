package com.plasmidEditor.sputnik.editor;

import org.biojava.nbio.core.sequence.location.SimpleLocation;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

public class AddMethod extends EditorMethod {
    @Override
    protected String getEditedSequence(String sequence, EditorParameters parameters) {
        StringBuilder sequenceBuilder = new StringBuilder(sequence);
        sequenceBuilder.insert(parameters.getPosition() - 1, parameters.getSubsequence());
        return sequenceBuilder.toString();
    }

    @Override
    protected <S extends AbstractSequence<C>, C extends Compound> void adjustLocations(S sequence, int position, int offset) {
        for (var f : sequence.getFeatures()) {
            int startPosition = f.getLocations().getStart().getPosition();
            int endPosition = f.getLocations().getEnd().getPosition();

            if (position < startPosition) {
                startPosition += offset;
                endPosition += offset;
            } else if (position < endPosition) {
                endPosition += offset;
            }
            f.setLocation(new SimpleLocation(startPosition, endPosition));
        }
    }
}
