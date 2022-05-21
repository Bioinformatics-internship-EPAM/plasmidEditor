package com.plasmidEditor.sputnik.editor;

import com.plasmidEditor.sputnik.exceptions.NewSequenceInstanceException;
import com.plasmidEditor.sputnik.exceptions.SequenceClassException;
import com.plasmidEditor.sputnik.exceptions.ValidationParametersException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.DataSource;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.TaxonomyID;
import org.biojava.nbio.core.sequence.features.DBReferenceInfo;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;


public abstract class EditorMethod {
    public <S extends AbstractSequence<C>, C extends Compound> S edit(EditorParameters parameters) {
        validationParameters (parameters);
        BufferedReader bufferedReader = new BufferedReader(new StringReader(parameters.getFileContent()));
        GenbankSequenceParser<S, C> parser = new GenbankSequenceParser<>();
        String originalSequence = parser.getSequence(bufferedReader, 0);

        Class<S> sequenceClass = getSequenceClass(parameters.getFileContent());
        String editedSequence = getEditedSequence(originalSequence, parameters);

        S sequence = createSequenceInstance(parser, editedSequence, sequenceClass);
        int offset = (parameters.getSubsequence() == null)
                ? parameters.getCutSize()
                : parameters.getSubsequence().length();

        adjustLocations(sequence, parameters.getPosition(), offset);

        return sequence;
    }

    protected abstract String getEditedSequence(String sequence, EditorParameters parameters);

    protected abstract <S extends AbstractSequence<C>, C extends Compound> void adjustLocations(
            S sequence, int position, int offset);

    private <S extends AbstractSequence<C>, C extends Compound> S createSequenceInstance(
            GenbankSequenceParser<S, C> parser, String sequence, Class<S> sequenceClass) {
        S newSequence;
        try {
            newSequence = sequenceClass
                    .getConstructor(String.class)
                    .newInstance(sequence);
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
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

    private Class getSequenceClass(String fileContent) {
        String[] locusElements = fileContent.split("\n")[0].split("\\s+");
        String unit = locusElements[3];
        String type = locusElements[4];
        if (unit.equalsIgnoreCase("aa"))
            return ProteinSequence.class;
        else if (unit.equalsIgnoreCase("bp") && type.equalsIgnoreCase("DNA"))
            return DNASequence.class;
        else
            throw new SequenceClassException("Failed to get sequence class");
    }

    private void validationParameters(EditorParameters parameters) {
        if (parameters.getPosition() < 0) {
            throw new ValidationParametersException("Error: position is negative");
        }
        if (parameters.getFileContent() == null || parameters.getFileContent().isEmpty()) {
            throw new ValidationParametersException("Error: the file content is empty");
        }
        if (parameters.getSubsequence() == null) {
            if (parameters.getCutSize() <= 0) {
                throw new ValidationParametersException("Error: cut size is negative or 0");
            }
        } else if (parameters.getSubsequence().isEmpty()) {
            throw new ValidationParametersException("Error: subsequence is empty");
        } else {
            Class temp = getSequenceClass(parameters.getFileContent());
            try {
                if (temp == ProteinSequence.class)
                    ProteinTools.createProtein(parameters.getSubsequence());
                else
                    DNATools.createDNA(parameters.getSubsequence());
            } catch (IllegalSymbolException e) {
                throw new ValidationParametersException("Error: subsequence has invalid symbols");
            }
        }
    }
}
