package com.plasmideditor.rocket.web.service.modifications;

import com.plasmideditor.rocket.web.exceptions.GenBankFileEditorException;
import com.plasmideditor.rocket.web.service.utils.FeatureUtils;
import com.plasmideditor.rocket.web.service.utils.SequenceFactory;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.features.AbstractFeature;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;

@Slf4j
@SuppressWarnings({"PMD.DataflowAnomalyAnalysis"})
public abstract class SequenceModification<S extends AbstractSequence<C>, C extends Compound> {

    final static String CAN_NOT_CREATE_SEQ = "Cannot create a sequence";

    abstract S modify(BufferedReader br,
                      int startPosition,
                      String sequence,
                      Class<S> cls,
                      S storedSequence,
                      GenbankSequenceParser<S, C> sequenceParser
    );


    public S runModificationProcess(BufferedReader br,
                                    int startPosition,
                                    String sequence,
                                    Class<S> cls
    ) {
        GenbankSequenceParser<S, C> sequenceParser = new GenbankSequenceParser<>();
        S storedSequence = createSequence(cls, sequenceParser.getSequence(br, 0));

        S newSequence = modify(br, startPosition, sequence, cls, storedSequence, sequenceParser);

        sequenceParser.getSequenceHeaderParser().parseHeader(sequenceParser.getHeader(), newSequence);
        return newSequence;
    }


    S createSequence(Class<S> cls, String sequence) {
        S storedSequence;
        try {
            storedSequence = (S) SequenceFactory.create(cls.getSimpleName(), sequence);
        } catch (CompoundNotFoundException e) {
            throw new GenBankFileEditorException(CAN_NOT_CREATE_SEQ, e);
        }
        return storedSequence;
    }

    void modifyFeaturesLocation(
            Map<String, List<AbstractFeature<AbstractSequence<C>, C>>> features,
            S newSequence,
            int start,
            int seqLength
    ) {
        for (String k : features.keySet()) {
            for (AbstractFeature<AbstractSequence<C>, C> f : features.get(k)) {
                if (FeatureUtils.getFeatureEnd(f) < start) {
                    newSequence.addFeature(f);
                } else {
                    updatePositionAfterModificationOperation(newSequence, start, seqLength, f);
                }
            }
        }
    }

    abstract void updatePositionAfterModificationOperation(
            S newSequence,
            int start,
            int seqLength,
            AbstractFeature<AbstractSequence<C>, C> f);

    S modifySequence(int startPosition, String sequence, Class<S> cls, S storedSequence) {
        S newSequence;
        try {
            newSequence = (S) SequenceFactory.create(cls.getSimpleName(),
                    createNewSequence(startPosition, sequence, storedSequence));
        } catch (CompoundNotFoundException e) {
            throw new GenBankFileEditorException(CAN_NOT_CREATE_SEQ, e);
        }
        return newSequence;
    }

    abstract String createNewSequence(int startPosition, String sequence, S storedSequence);

}
