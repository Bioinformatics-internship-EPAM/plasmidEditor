package com.plasmideditor.rocket.web.service.modifications;

import com.plasmideditor.rocket.web.service.exceptions.GenBankFileEditorException;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.sequence.features.AbstractFeature;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

import java.io.BufferedReader;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class ModifyModification extends SequenceModification {
    private final String SEQ_LEN_OUT_OF_RANGE = "Modified sequence is out of range";

    @Override
    <S extends AbstractSequence<C>, C extends Compound> S modify(BufferedReader br, int startPosition, String sequence, Class<S> cls, S storedSequence, GenbankSequenceParser<S, C> sequenceParser) throws GenBankFileEditorException {
        if (startPosition + sequence.length() > storedSequence.getLength()) {
            log.error(SEQ_LEN_OUT_OF_RANGE);
            throw new GenBankFileEditorException(SEQ_LEN_OUT_OF_RANGE);
        }

        return modifySequence(startPosition, sequence, cls, storedSequence);
    }

    @Override
    <S extends AbstractSequence<C>, C extends Compound> void updatePositionAfterModificationOperation(S newSequence,
                                                                                                      int start,
                                                                                                      int seqLength,
                                                                                                      AbstractFeature<AbstractSequence<C>, C> f,
                                                                                                      int featureStartPosition,
                                                                                                      int featureEndPosition) {
    }

    private <S extends AbstractSequence<C>, C extends Compound> S modifySequence(int startPosition, String sequence, Class<S> cls, S storedSequence) throws GenBankFileEditorException {
        S newSequence;
        try {
            newSequence = cls.getConstructor(String.class).newInstance(
                    storedSequence.getSequenceAsString().substring(0, startPosition) +
                            sequence +
                            storedSequence.getSequenceAsString().substring(startPosition + sequence.length())
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new GenBankFileEditorException(CAN_NOT_CREATE_SEQ, e);
        }
        return newSequence;
    }
}
