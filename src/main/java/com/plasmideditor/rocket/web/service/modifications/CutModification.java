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
    @Override
    public <S extends AbstractSequence<C>, C extends Compound> S modify(BufferedReader br,
                                                                        int startPosition,
                                                                        String sequence,
                                                                        Class<S> cls,
                                                                        S storedSequence,
                                                                        GenbankSequenceParser<S, C> sequenceParser
    ) throws GenBankFileEditorException {

        if (!storedSequence.getSequenceAsString().toLowerCase().startsWith(sequence.toLowerCase(), startPosition)) {
            log.error("Illegal start position or sequence to delete: {}, {}", startPosition, sequence);
            throw new GenBankFileEditorException("Illegal start position or sequence to delete: " + startPosition + ", " + sequence);
        }

        S newSequence = cutFromSequence(startPosition, sequence, cls, storedSequence);
        modifyFeaturesLocation(sequenceParser, newSequence, startPosition, sequence.length());

        return newSequence;
    }

    @Override
    public <S extends AbstractSequence<C>, C extends Compound> void updatePositionAfterModificationOperation(S newSequence, int start, int seqLength, AbstractFeature<AbstractSequence<C>, C> f, int featureStartPosition, int featureEndPosition) {
        if (featureStartPosition < start) {
            if (isFeaturePositionBetweenSequenceStartAndEnd(start, seqLength, featureEndPosition)) {
                f.setLocation(new SimpleLocation(featureStartPosition, start));
                newSequence.addFeature(f);
            }
            if (isFeaturePositionAfterSequenceEnd(start, seqLength, featureEndPosition)) {
                f.setLocation(new SimpleLocation(featureStartPosition, featureEndPosition - seqLength));
                newSequence.addFeature(f);
            }
        }
        if (isFeaturePositionBetweenSequenceStartAndEnd(start, seqLength, featureStartPosition)) {
            if (isFeaturePositionAfterSequenceEnd(start, seqLength, featureEndPosition)) {
                f.setLocation(new SimpleLocation(start, featureEndPosition - seqLength));
                newSequence.addFeature(f);
            }
        }
        if (isFeaturePositionAfterSequenceEnd(start, seqLength, featureStartPosition)) {
            f.setLocation(new SimpleLocation(featureStartPosition - seqLength, featureEndPosition - seqLength));
            newSequence.addFeature(f);
        }
    }

    private <S extends AbstractSequence<C>, C extends Compound> S cutFromSequence(int startPosition, String sequence, Class<S> cls, S storedSequence) throws GenBankFileEditorException {
        S newSequence;
        try {
            newSequence = cls.getConstructor(String.class).newInstance(
                    storedSequence.getSequenceAsString().substring(0, startPosition) +
                            storedSequence.getSequenceAsString().substring(startPosition + sequence.length())
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new GenBankFileEditorException(CAN_NOT_CREATE_SEQ, e);
        }
        return newSequence;
    }

    private boolean isFeaturePositionAfterSequenceEnd(int start, int seqLength, int featurePosition) {
        return featurePosition > start + seqLength;
    }

    private boolean isFeaturePositionBetweenSequenceStartAndEnd(int start, int seqLength, int featurePosition) {
        return featurePosition >= start && featurePosition <= start + seqLength;
    }
}
