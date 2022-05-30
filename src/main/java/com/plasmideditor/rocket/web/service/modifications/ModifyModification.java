package com.plasmideditor.rocket.web.service.modifications;

import com.plasmideditor.rocket.web.exceptions.GenBankFileEditorException;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.sequence.features.AbstractFeature;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

import java.io.BufferedReader;

@Slf4j
public class ModifyModification<S extends AbstractSequence<C>, C extends Compound> extends SequenceModification<S, C> {
    private static final String SEQ_LEN_OUT_OF_RANGE = "Modified sequence is out of range";

    @Override
    S modify(BufferedReader br,
             int startPosition,
             String sequence,
             Class<S> cls,
             S storedSequence,
             GenbankSequenceParser<S, C> sequenceParser) {
        if (startPosition + sequence.length() > storedSequence.getLength()) {
            log.error(SEQ_LEN_OUT_OF_RANGE);
            throw new GenBankFileEditorException(SEQ_LEN_OUT_OF_RANGE);
        }

        return modifySequence(startPosition, sequence, cls, storedSequence);
    }

    @Override
    void updatePositionAfterModificationOperation(S newSequence,
                                                  int start,
                                                  int seqLength,
                                                  AbstractFeature<AbstractSequence<C>, C> f) {
    }

    @Override
    String createNewSequence(
            int startPosition, String sequence, S storedSequence) {
        return storedSequence.getSequenceAsString().substring(0, startPosition) +
                sequence +
                storedSequence.getSequenceAsString().substring(startPosition + sequence.length());
    }
}
