package com.plasmideditor.rocket.web.service.modifications;

import com.plasmideditor.rocket.web.service.exceptions.GenBankFileEditorException;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.sequence.features.AbstractFeature;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;
import org.biojava.nbio.core.sequence.location.SimpleLocation;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

import java.io.BufferedReader;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class CutModification extends SequenceModification {
    private final String ILLEGAL_START_POSITION = "Illegal start position or sequence to delete:";
    @Override
    public <S extends AbstractSequence<C>, C extends Compound> S modify(BufferedReader br,
                                                                        int startPosition,
                                                                        String sequence,
                                                                        Class<S> cls,
                                                                        S storedSequence,
                                                                        GenbankSequenceParser<S, C> sequenceParser
    ) throws GenBankFileEditorException {

        if (!storedSequence.getSequenceAsString().toLowerCase().startsWith(sequence.toLowerCase(), startPosition)) {
            String illegualStartPosition = ILLEGAL_START_POSITION + startPosition + ", " + sequence;
            log.error(illegualStartPosition);
            throw new GenBankFileEditorException(illegualStartPosition);
        }

        S newSequence = modifySequence(startPosition, sequence, cls, storedSequence);
        modifyFeaturesLocation(sequenceParser.getFeatures(), newSequence, startPosition, sequence.length());

        return newSequence;
    }

    @Override
    public <S extends AbstractSequence<C>, C extends Compound> void updatePositionAfterModificationOperation(S newSequence,
                                                                                                             int start,
                                                                                                             int seqLength,
                                                                                                             AbstractFeature<AbstractSequence<C>, C> f,
                                                                                                             int featureStartPosition,
                                                                                                             int featureEndPosition) {
        int startPosition = featureStartPosition;
        int endPosition = featureEndPosition;
        if (isFeaturePositionBetweenSequenceStartAndEnd(start, seqLength, featureStartPosition)) {
            if (isFeaturePositionBetweenSequenceStartAndEnd(start, seqLength, featureEndPosition)){
                return;
            }
            startPosition = start;
        }
        if (isFeaturePositionAfterSequenceEnd(start, seqLength, featureStartPosition)) {
            startPosition -= seqLength;
        }
        if (isFeaturePositionAfterSequenceEnd(start, seqLength, featureEndPosition)) {
            endPosition -= seqLength;
        }
        if (isFeaturePositionBetweenSequenceStartAndEnd(start, seqLength, featureEndPosition)) {
            endPosition = start;
        }
        f.setLocation(new SimpleLocation(startPosition, endPosition));
        newSequence.addFeature(f);
    }

    @Override
    <S extends AbstractSequence<C>, C extends Compound> String createNewSequence(
            int startPosition, String sequence, S storedSequence) {
        return storedSequence.getSequenceAsString().substring(0, startPosition) +
                storedSequence.getSequenceAsString().substring(startPosition + sequence.length());
    }

    private boolean isFeaturePositionAfterSequenceEnd(int start, int seqLength, int featurePosition) {
        return featurePosition > start + seqLength;
    }

    private boolean isFeaturePositionBetweenSequenceStartAndEnd(int start, int seqLength, int featurePosition) {
        return featurePosition >= start && featurePosition <= start + seqLength;
    }
}
