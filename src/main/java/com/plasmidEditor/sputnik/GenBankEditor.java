package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.exceptions.NewSequenceInstanceException;
import lombok.val;
import org.biojava.nbio.core.sequence.DataSource;
import org.biojava.nbio.core.sequence.TaxonomyID;
import org.biojava.nbio.core.sequence.features.DBReferenceInfo;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;
import org.biojava.nbio.core.sequence.location.SimpleLocation;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;

public class GenBankEditor {
    private enum EditorType {
        ADD_METHOD,
        MODIFY_METHOD,
        CUT_METHOD
    }

    public <S extends AbstractSequence<C>, C extends Compound> S add(
            String subSequence, int position, String genbankContent, Class<S> sequenceClass) {
        return editorMethod(subSequence, position, genbankContent, sequenceClass, EditorType.ADD_METHOD);
    }

    public <S extends AbstractSequence<C>, C extends Compound> S modify(
            String subSequence, int position, String genbankContent, Class<S> sequenceClass) {
        return editorMethod(subSequence, position, genbankContent, sequenceClass, EditorType.MODIFY_METHOD);
    }

    public <S extends AbstractSequence<C>, C extends Compound> S cut(
            int length, int position, String genbankContent, Class<S> sequenceClass) {
        String subSequence = "";
        for (int i = 0; i < length; i++)
            subSequence += "a";
        return editorMethod(subSequence, position, genbankContent, sequenceClass, EditorType.CUT_METHOD);
    }

    private <S extends AbstractSequence<C>, C extends Compound> S editorMethod
            (String subSequence, int position, String genbankContent, Class<S> sequenceClass, EditorType editorType) {
        int offset = subSequence.length();
        BufferedReader bufferedReader = new BufferedReader(new StringReader(genbankContent));
        GenbankSequenceParser<S, C> parser = new GenbankSequenceParser<>();

        StringBuilder sequenceBuilder = new StringBuilder(parser.getSequence(bufferedReader, 0));

        switch (editorType) {
            case ADD_METHOD: sequenceBuilder.insert(position - 1, subSequence); break;
            case MODIFY_METHOD: sequenceBuilder.replace(position - 1, position - 1 + offset, subSequence); break;
            case CUT_METHOD: sequenceBuilder.delete(position - 1, position - 1 + offset); break;
        }

        S sequence = createNewSequenceInstance(sequenceBuilder.toString(), parser, sequenceClass);
        adjustLocations(sequence, position, offset, editorType);

        return sequence;
    }

    private <S extends AbstractSequence<C>, C extends Compound> S createNewSequenceInstance(
            String sequence, GenbankSequenceParser<S,C> parser, Class<S> sequenceClass) {
        S newSequence;
        try {
            newSequence = sequenceClass.getConstructor(String.class).newInstance(sequence);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new NewSequenceInstanceException("Failed to create an instance", e);
        }

        parser.getSequenceHeaderParser().parseHeader(parser.getHeader(), newSequence);
        parser.parseFeatures(newSequence);

        DBReferenceInfo dbQualifier = parser.getDatabaseReferences().get("db_xref").get(0);
        if (dbQualifier != null) {
            newSequence.setTaxonomy(
                    new TaxonomyID(dbQualifier.getDatabase() + ":" + dbQualifier.getId(), DataSource.GENBANK));
        }

        return newSequence;
    }

    private <S extends AbstractSequence<C>, C extends Compound> void adjustLocations
            (S sequence, int position, int offset, EditorType editorType) {
        switch (editorType) {
            case MODIFY_METHOD:
                return;
            case ADD_METHOD:
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
                break;
            case CUT_METHOD:
                for (var f: sequence.getFeatures()) {
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
                            // TODO: cut the feature
                        }
                    } else if (position < endPosition) {
                        if (position + offset < endPosition)
                            endPosition -= offset;
                        else
                            endPosition = position;
                    }

                    f.setLocation(new SimpleLocation(startPosition, endPosition));
                }
                break;
        }
    }
}
