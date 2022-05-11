package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.exceptions.NewSequenceInstanceException;
import org.biojava.nbio.core.sequence.DataSource;
import org.biojava.nbio.core.sequence.TaxonomyID;
import org.biojava.nbio.core.sequence.features.DBReferenceInfo;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;

public class GenBankEditor {
    public <S extends AbstractSequence<C>, C extends Compound> S add(
            String sequence, int position, String genbankContent, Class<S> sequenceClass) {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(genbankContent));
        GenbankSequenceParser<S, C> parser = new GenbankSequenceParser<>();

        StringBuilder sequenceBuilder = new StringBuilder(parser.getSequence(bufferedReader, 0));
        sequenceBuilder.insert(position - 1, sequence);

        S newSequence;
        try {
            newSequence = sequenceClass.getConstructor(String.class).newInstance(sequenceBuilder.toString());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new NewSequenceInstanceException("Failed to create an instance", e);
        }

        parser.getSequenceHeaderParser().parseHeader(parser.getHeader(), newSequence);
        adjustLocations(newSequence, position, sequence.length(), true);

        DBReferenceInfo dbQualifier = parser.getDatabaseReferences().get("db_xref").get(0);
        if (dbQualifier != null) {
            newSequence.setTaxonomy(
                    new TaxonomyID(dbQualifier.getDatabase() + ":" + dbQualifier.getId(), DataSource.GENBANK));
        }

        // TODO set accession

        return newSequence;
    }

    private <S extends AbstractSequence<C>, C extends Compound> void adjustLocations(
            S sequence, int position, int offset, boolean increase) {
        // TODO adjust locations
    }
}
