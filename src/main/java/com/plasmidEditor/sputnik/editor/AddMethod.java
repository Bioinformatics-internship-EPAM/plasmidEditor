package com.plasmidEditor.sputnik.editor;

import org.biojava.nbio.core.sequence.location.SimpleLocation;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

public class AddMethod extends EditorMethod {
    @Override
    protected String getEditedSequence(String sequence, String subsequence, int position, int cutSize) {
        StringBuilder sequenceBuilder = new StringBuilder(sequence);
        sequenceBuilder.insert(position - 1, subsequence);
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
