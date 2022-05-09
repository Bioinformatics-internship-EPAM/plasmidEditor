package com.plasmideditor.rocket.genbank.services;

import com.plasmideditor.rocket.genbank.services.exceptions.GenBankFileEditorException;
import com.plasmideditor.rocket.genbank.services.exceptions.SequenceValidationException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.DataSource;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.TaxonomyID;
import org.biojava.nbio.core.sequence.features.AbstractFeature;
import org.biojava.nbio.core.sequence.features.DBReferenceInfo;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;
import org.biojava.nbio.core.sequence.io.GenbankWriterHelper;
import org.biojava.nbio.core.sequence.location.SimpleLocation;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

@Service
public class EditService {
    private final String bpRegex = "[atcg]+";
    private final String aaRegex = "[arndcqeghilkmfpsrwyv]+";

    private final String ADD_OPERATION = "ADD";
    private final String CUT_OPERATION = "CUT";

//    private final GenBankRepository genBankRepository;
//
//    public CutService(GenBankRepository genBankRepository) {
//        this.genBankRepository = genBankRepository;
//    }

    private String getFileFromDB(String id, String version) {
//        GenBankEntity genBankFile = genBankRepository.findByGenbankId(id, version);
//        return genbankFile.getFile();
        return "";
    }

    private void saveFileToDB(String id, String version, String file) {

    }

    public String cutGenBankFile(int startPosition, String sequence, String id, String version) throws GenBankFileEditorException {
        String fileContent = getFileFromDB(id, version);

        String type = fileContent.split("\n")[0].split(" +")[3];
        BufferedReader br = new BufferedReader(new StringReader(fileContent));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (type.equalsIgnoreCase("aa")) {
            ProteinSequence newSequence = cut(br, startPosition, sequence, ProteinSequence.class);
            try {
                GenbankWriterHelper.writeProteinSequence(byteArrayOutputStream, Collections.singleton(newSequence));
            } catch (Exception e) {
                throw new GenBankFileEditorException("Cannot write sequence", e);
            }
        } else if (type.equalsIgnoreCase("bp")) {
            DNASequence newSequence = cut(br, startPosition, sequence, DNASequence.class);
            try {
                GenbankWriterHelper.writeNucleotideSequence(byteArrayOutputStream, Collections.singleton(newSequence));
            } catch (Exception e) {
                throw new GenBankFileEditorException("Cannot write sequence", e);
            }
        }

        saveFileToDB(id, version, byteArrayOutputStream.toString());
        return byteArrayOutputStream.toString();
    }

    public String modifyGenBankFile(int startPosition, String sequence, String id, String version) throws GenBankFileEditorException, SequenceValidationException {
        String fileContent = getFileFromDB(id, version);
        String type = fileContent.split("\n")[0].split(" +")[3];
        BufferedReader br = new BufferedReader(new StringReader(fileContent));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (type.equalsIgnoreCase("aa")) {
            if (!sequence.toLowerCase().matches(aaRegex)) {
                throw new SequenceValidationException("Illegal amino acid residues in sequence " + sequence,
                        SequenceValidationException.ExpectedType.PROTEIN);
            }
            ProteinSequence newSequence = modify(br, startPosition, sequence, ProteinSequence.class);
            try {
                GenbankWriterHelper.writeProteinSequence(byteArrayOutputStream, Collections.singleton(newSequence));
            } catch (Exception e) {
                throw new GenBankFileEditorException("Cannot write sequence", e);
            }
        } else if (type.equalsIgnoreCase("bp")) {
            if (!sequence.toLowerCase().matches(bpRegex)) {
                throw new SequenceValidationException("Illegal nucleotide base pair in sequence " + sequence,
                        SequenceValidationException.ExpectedType.DNA);
            }
            DNASequence newSequence = modify(br, startPosition, sequence, DNASequence.class);
            try {
                GenbankWriterHelper.writeNucleotideSequence(byteArrayOutputStream, Collections.singleton(newSequence));
            } catch (Exception e) {
                throw new GenBankFileEditorException("Cannot write sequence", e);
            }
        }

        saveFileToDB(id, version, byteArrayOutputStream.toString());
        return byteArrayOutputStream.toString();
    }

    public String addGenBankFile(int startPosition, String sequence, String id, String version) throws GenBankFileEditorException, SequenceValidationException {
        String fileContent = getFileFromDB(id, version);
        String type = fileContent.split("\n")[0].split(" +")[3];
        BufferedReader br = new BufferedReader(new StringReader(fileContent));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (type.equalsIgnoreCase("aa")) {
            if (!sequence.toLowerCase().matches(aaRegex)) {
                throw new SequenceValidationException("Illegal amino acid residues in sequence " + sequence,
                        SequenceValidationException.ExpectedType.PROTEIN);
            }
            ProteinSequence newSequence = add(br, startPosition, sequence, ProteinSequence.class);
            try {
                GenbankWriterHelper.writeProteinSequence(byteArrayOutputStream, Collections.singleton(newSequence));
            } catch (Exception e) {
                throw new GenBankFileEditorException("Cannot write sequence", e);
            }
        } else if (type.equalsIgnoreCase("bp")) {
            if (!sequence.toLowerCase().matches(bpRegex)) {
                throw new SequenceValidationException("Illegal nucleotide base pair in sequence " + sequence,
                        SequenceValidationException.ExpectedType.DNA);
            }
            DNASequence newSequence = add(br, startPosition, sequence, DNASequence.class);
            try {
                GenbankWriterHelper.writeNucleotideSequence(byteArrayOutputStream, Collections.singleton(newSequence));
            } catch (Exception e) {
                throw new GenBankFileEditorException("Cannot write sequence", e);
            }
        }

        saveFileToDB(id, version, byteArrayOutputStream.toString());
        return byteArrayOutputStream.toString();
    }

    private <S extends AbstractSequence<C>, C extends Compound> S cut(
            BufferedReader br,
            int startPosition,
            String sequence,
            Class<S> cls
    ) throws GenBankFileEditorException {
        GenbankSequenceParser<S, C> sequenceParser = new GenbankSequenceParser<>();
        S storedSequence = getStoredSequence(cls, sequenceParser.getSequence(br, 0));

        if (!storedSequence.getSequenceAsString().toLowerCase().startsWith(sequence, startPosition)) {
            throw new GenBankFileEditorException("Illegal start position or sequence to delete: " + startPosition + ", " + sequence);
        }

        S newSequence = cutFromSequence(startPosition, sequence, cls, storedSequence);
        sequenceParser.getSequenceHeaderParser().parseHeader(sequenceParser.getHeader(), newSequence);
        modifyFeaturesLocation(sequenceParser, newSequence, startPosition, sequence.length(), CUT_OPERATION);
        List<DBReferenceInfo> dbQualifier = sequenceParser.getDatabaseReferences().get("db_xref");
        if (dbQualifier != null) {
            DBReferenceInfo q = dbQualifier.get(0);
            newSequence.setTaxonomy(new TaxonomyID(q.getDatabase() + ":" + q.getId(), DataSource.GENBANK));
        }

        return newSequence;
    }

    private <S extends AbstractSequence<C>, C extends Compound> S modify(
            BufferedReader br,
            int startPosition,
            String sequence,
            Class<S> cls
    ) throws GenBankFileEditorException {
        GenbankSequenceParser<S, C> sequenceParser = new GenbankSequenceParser<>();
        S storedSequence = getStoredSequence(cls, sequenceParser.getSequence(br, 0));

        if (startPosition + sequence.length() > storedSequence.getLength()) {
            throw new GenBankFileEditorException("Modified sequence is too long");
        }

        S newSequence = modifySequence(startPosition, sequence, cls, storedSequence);
        sequenceParser.getSequenceHeaderParser().parseHeader(sequenceParser.getHeader(), newSequence);
        for (String k : sequenceParser.getFeatures().keySet()) {
            for (AbstractFeature<AbstractSequence<C>, C> f : sequenceParser.getFeatures().get(k)) {
                newSequence.addFeature(f);
            }
        }
        List<DBReferenceInfo> dbQualifier = sequenceParser.getDatabaseReferences().get("db_xref");
        if (dbQualifier != null) {
            DBReferenceInfo q = dbQualifier.get(0);
            newSequence.setTaxonomy(new TaxonomyID(q.getDatabase() + ":" + q.getId(), DataSource.GENBANK));
        }

        return newSequence;
    }

    private <S extends AbstractSequence<C>, C extends Compound> S add(
            BufferedReader br,
            int startPosition,
            String sequence,
            Class<S> cls
    ) throws GenBankFileEditorException {
        GenbankSequenceParser<S, C> sequenceParser = new GenbankSequenceParser<>();
        S storedSequence = getStoredSequence(cls, sequenceParser.getSequence(br, 0));

        S newSequence = addToSequence(startPosition, sequence, cls, storedSequence);
        sequenceParser.getSequenceHeaderParser().parseHeader(sequenceParser.getHeader(), newSequence);
        modifyFeaturesLocation(sequenceParser, newSequence, startPosition, sequence.length(), ADD_OPERATION);
        List<DBReferenceInfo> dbQualifier = sequenceParser.getDatabaseReferences().get("db_xref");
        if (dbQualifier != null) {
            DBReferenceInfo q = dbQualifier.get(0);
            newSequence.setTaxonomy(new TaxonomyID(q.getDatabase() + ":" + q.getId(), DataSource.GENBANK));
        }

        return newSequence;
    }

    private <S extends AbstractSequence<C>, C extends Compound> S addToSequence(int startPosition, String sequence, Class<S> cls, S storedSequence) throws GenBankFileEditorException {
        S newSequence;
        try {
            newSequence = cls.getConstructor(String.class).newInstance(
                    storedSequence.getSequenceAsString().substring(0, startPosition) +
                            sequence +
                            storedSequence.getSequenceAsString().substring(startPosition)
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new GenBankFileEditorException("Cannot create a sequence", e);
        }
        return newSequence;
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
            throw new GenBankFileEditorException("Cannot create a sequence", e);
        }
        return newSequence;
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
            throw new GenBankFileEditorException("Cannot create a sequence", e);
        }
        return newSequence;
    }

    private <S extends AbstractSequence<C>, C extends Compound> void modifyFeaturesLocation(
            GenbankSequenceParser<S, C> sequenceParser,
            S newSequence,
            int start,
            int seqLength,
            String operation
    ) throws GenBankFileEditorException {
        for (String k : sequenceParser.getFeatures().keySet()) {
            for (AbstractFeature<AbstractSequence<C>, C> f : sequenceParser.getFeatures().get(k)) {
                int featureStartPosition = f.getLocations().getStart().getPosition();
                int featureEndPosition = f.getLocations().getEnd().getPosition();
                if (featureEndPosition < start) {
                    newSequence.addFeature(f);
                }
                if (operation.equals(CUT_OPERATION)) {
                    updatePositionAfterCutOperation(newSequence, start, seqLength, f, featureStartPosition, featureEndPosition);
                } else if (operation.equals(ADD_OPERATION)) {
                    updatePositionAfterAddOperation(newSequence, start, seqLength, f, featureStartPosition, featureEndPosition);
                } else {
                    throw new GenBankFileEditorException("Unknown modification operation");
                }
            }
        }
    }

    private <S extends AbstractSequence<C>, C extends Compound> void updatePositionAfterAddOperation(S newSequence, int start, int seqLength, AbstractFeature<AbstractSequence<C>, C> f, int featureStartPosition, int featureEndPosition) {
        if (featureStartPosition < start) {
            f.setLocation(new SimpleLocation(featureStartPosition, featureEndPosition + seqLength));
            newSequence.addFeature(f);
        }
        if (featureStartPosition >= start) {
            f.setLocation(new SimpleLocation(featureStartPosition + seqLength, featureEndPosition + seqLength));
            newSequence.addFeature(f);
        }
    }

    private <S extends AbstractSequence<C>, C extends Compound> void updatePositionAfterCutOperation(S newSequence, int start, int seqLength, AbstractFeature<AbstractSequence<C>, C> f, int featureStartPosition, int featureEndPosition) {
        if (featureStartPosition < start) {
            if (isFeatureEndBetweenSequenceStartAndEnd(start, seqLength, featureEndPosition)) {
                f.setLocation(new SimpleLocation(featureStartPosition, start));
                newSequence.addFeature(f);
            }
            if (isFeaturePositionAfterSequenceEnd(start, seqLength, featureEndPosition)) {
                f.setLocation(new SimpleLocation(featureStartPosition, featureEndPosition - seqLength));
                newSequence.addFeature(f);
            }
        }
        if (isFeatureEndBetweenSequenceStartAndEnd(start, seqLength, featureStartPosition)) {
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

    private boolean isFeaturePositionAfterSequenceEnd(int start, int seqLength, int featurePosition) {
        return featurePosition > start + seqLength;
    }

    private boolean isFeatureEndBetweenSequenceStartAndEnd(int start, int seqLength, int featureEndPosition) {
        return featureEndPosition > start && featureEndPosition < start + seqLength;
    }

    private <S extends AbstractSequence<C>, C extends Compound> S getStoredSequence(Class<S> cls, String sequenceParser) throws GenBankFileEditorException {
        S storedSequence;
        try {
            storedSequence = cls.getConstructor(String.class).newInstance(sequenceParser);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new GenBankFileEditorException("Cannot create a sequence", e);
        }
        return storedSequence;
    }
}
