package com.plasmideditor.rocket.web.service.modifications;

import com.plasmideditor.rocket.web.service.exceptions.GenBankFileEditorException;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.sequence.DataSource;
import org.biojava.nbio.core.sequence.TaxonomyID;
import org.biojava.nbio.core.sequence.features.AbstractFeature;
import org.biojava.nbio.core.sequence.features.DBReferenceInfo;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

import java.io.BufferedReader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class SequenceModification {

    final String CAN_NOT_CREATE_SEQ = "Cannot create a sequence";

    abstract <S extends AbstractSequence<C>, C extends Compound> S modify(BufferedReader br,
                                                                          int startPosition,
                                                                          String sequence,
                                                                          Class<S> cls,
                                                                          S storedSequence,
                                                                          GenbankSequenceParser<S, C> sequenceParser
    ) throws GenBankFileEditorException;


    public <S extends AbstractSequence<C>, C extends Compound> S runModificationProcess(BufferedReader br,
                                                                                        int startPosition,
                                                                                        String sequence,
                                                                                        Class<S> cls
    ) throws GenBankFileEditorException {
        GenbankSequenceParser<S, C> sequenceParser = new GenbankSequenceParser<>();
        S storedSequence = createSequence(cls, sequenceParser.getSequence(br, 0));

        S newSequence = modify(br, startPosition, sequence, cls, storedSequence, sequenceParser);

        sequenceParser.getSequenceHeaderParser().parseHeader(sequenceParser.getHeader(), newSequence);
        return newSequence;
    }


    <S extends AbstractSequence<C>, C extends Compound> S createSequence(Class<S> cls, String sequenceParser) throws GenBankFileEditorException {
        S storedSequence;
        try {
            storedSequence = cls.getConstructor(String.class).newInstance(sequenceParser);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new GenBankFileEditorException(CAN_NOT_CREATE_SEQ, e);
        }
        return storedSequence;
    }

    <S extends AbstractSequence<C>, C extends Compound> void modifyFeaturesLocation(
            Map<String, List<AbstractFeature<AbstractSequence<C>, C>>> features,
            S newSequence,
            int start,
            int seqLength
    ) {
        for (String k : features.keySet()) {
            for (AbstractFeature<AbstractSequence<C>, C> f : features.get(k)) {
                int featureStartPosition = f.getLocations().getStart().getPosition();
                int featureEndPosition = f.getLocations().getEnd().getPosition();
                if (featureEndPosition < start) {
                    newSequence.addFeature(f);
                } else {
                    updatePositionAfterModificationOperation(newSequence, start, seqLength, f, featureStartPosition, featureEndPosition);
                }
            }
        }
    }

    abstract <S extends AbstractSequence<C>, C extends Compound> void updatePositionAfterModificationOperation(
            S newSequence,
            int start,
            int seqLength,
            AbstractFeature<AbstractSequence<C>, C> f,
            int featureStartPosition,
            int featureEndPosition);

    <S extends AbstractSequence<C>, C extends Compound> S modifySequence(int startPosition, String sequence, Class<S> cls, S storedSequence) throws GenBankFileEditorException {
        S newSequence;
        try {
            newSequence = cls.getConstructor(String.class).newInstance(
                    createNewSequence(startPosition, sequence, storedSequence)
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new GenBankFileEditorException(CAN_NOT_CREATE_SEQ, e);
        }
        return newSequence;
    }

    abstract <S extends AbstractSequence<C>, C extends Compound> String createNewSequence(
            int startPosition, String sequence, S storedSequence);

}
