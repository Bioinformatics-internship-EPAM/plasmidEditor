package com.plasmidEditor.sputnik.editor;

import org.biojava.nbio.core.sequence.location.SimpleLocation;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

public class CutMethod extends EditorMethod {
    @Override
    protected String getEditedSequence(String sequence, String subsequence, int position, int cutSize) {
        StringBuilder sequenceBuilder = new StringBuilder(sequence);
        sequenceBuilder.delete(
                position - 1,
                position - 1 + cutSize);
        return sequenceBuilder.toString();
    }

    @Override
    protected <S extends AbstractSequence<C>, C extends Compound> void adjustLocations(
            S sequence, int position, int offset) {
        for (var f : sequence.getFeatures()) {
            int startPosition = f.getLocations().getStart().getPosition();
            int endPosition = f.getLocations().getEnd().getPosition();

            if (position < startPosition) {
                if (position + offset < startPosition) {
                    startPosition -= offset;
                    endPosition -= offset;
                } else if (position + offset < endPosition) {
                    startPosition = position;
                    endPosition -= offset;
                } else {
                    startPosition = endPosition = position;
                }
            } else if (position < endPosition) {
                if (position + offset < endPosition)
                    endPosition -= offset;
                else
                    endPosition = position;
            }

            f.setLocation(new SimpleLocation(startPosition, endPosition));
        }
    }
}
