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
public class AddModification extends SequenceModification {
    @Override
    public <S extends AbstractSequence<C>, C extends Compound> S modify(BufferedReader br,
                                                                        int startPosition,
                                                                        String sequence,
                                                                        Class<S> cls,
                                                                        S storedSequence,
                                                                        GenbankSequenceParser<S, C> sequenceParser
    ) throws GenBankFileEditorException {

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
        if (featureStartPosition > start) {
            startPosition += seqLength;
        }
        f.setLocation(new SimpleLocation(startPosition, featureEndPosition + seqLength));
        newSequence.addFeature(f);
    }

    @Override
    <S extends AbstractSequence<C>, C extends Compound> String createNewSequence(
            int startPosition, String sequence, S storedSequence) {
        return storedSequence.getSequenceAsString().substring(0, startPosition) +
                sequence +
                storedSequence.getSequenceAsString().substring(startPosition);
    }
}
