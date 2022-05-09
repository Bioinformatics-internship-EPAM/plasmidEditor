package com.plasmideditor.rocket.genbank.services;

import com.plasmideditor.rocket.genbank.services.exceptions.GenBankFileEditorException;
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

    public String modifyGenBankFile(int startPosition, String sequence, String id, String version) throws GenBankFileEditorException {
        String fileContent = getFileFromDB(id, version);

        String type = fileContent.split("\n")[0].split(" +")[3];
        BufferedReader br = new BufferedReader(new StringReader(fileContent));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (type.equalsIgnoreCase("aa")) {
            if (!sequence.toLowerCase().matches(aaRegex)) {
                throw new GenBankFileEditorException("Illegal amino acid residues in sequence " + sequence);
            }
            ProteinSequence newSequence = modify(br, startPosition, sequence, ProteinSequence.class);
            try {
                GenbankWriterHelper.writeProteinSequence(byteArrayOutputStream, Collections.singleton(newSequence));
            } catch (Exception e) {
                throw new GenBankFileEditorException("Cannot write sequence", e);
            }
        } else if (type.equalsIgnoreCase("bp")) {
            if (!sequence.toLowerCase().matches(bpRegex)) {
                throw new GenBankFileEditorException("Illegal nucleotide base pair in sequence " + sequence);
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

    public <S extends AbstractSequence<C>, C extends Compound> S cut(
            BufferedReader br,
            int startPosition,
            String sequence,
            Class<S> cls
    ) throws GenBankFileEditorException {
        GenbankSequenceParser<S, C> sequenceParser = new GenbankSequenceParser<>();
        S storedSequence;
        try {
            storedSequence = cls.getConstructor(String.class).newInstance(sequenceParser.getSequence(br, 0));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new GenBankFileEditorException("Cannot create a sequence", e);
        }

        if (!storedSequence.getSequenceAsString().toLowerCase().startsWith(sequence, startPosition)) {
            throw new GenBankFileEditorException("Illegal start position or sequence to delete: " + startPosition + ", " + sequence);
        }

        S newSequence;
        try {
            newSequence = cls.getConstructor(String.class).newInstance(
                    storedSequence.getSequenceAsString().substring(0, startPosition) +
                            storedSequence.getSequenceAsString().substring(startPosition + sequence.length())
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new GenBankFileEditorException("Cannot create a sequence", e);
        }
        sequenceParser.getSequenceHeaderParser().parseHeader(sequenceParser.getHeader(), newSequence);
        modifyFeaturesLocationAfterCutting(sequenceParser, newSequence, startPosition, sequence.length());
        List<DBReferenceInfo> dbQualifier = sequenceParser.getDatabaseReferences().get("db_xref");
        if (dbQualifier != null) {
            DBReferenceInfo q = dbQualifier.get(0);
            newSequence.setTaxonomy(new TaxonomyID(q.getDatabase() + ":" + q.getId(), DataSource.GENBANK));
        }

        return newSequence;
    }

    public <S extends AbstractSequence<C>, C extends Compound> S modify(
            BufferedReader br,
            int startPosition,
            String sequence,
            Class<S> cls
    ) throws GenBankFileEditorException {
        GenbankSequenceParser<S, C> sequenceParser = new GenbankSequenceParser<>();
        S storedSequence;
        try {
            storedSequence = cls.getConstructor(String.class).newInstance(sequenceParser.getSequence(br, 0));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new GenBankFileEditorException("Cannot create a sequence", e);
        }

        if (startPosition + sequence.length() > storedSequence.getLength()) {
            throw new GenBankFileEditorException("Modified sequence is too long");
        }

        S newSequence;
        try {
            newSequence = cls.getConstructor(String.class).newInstance(
                    storedSequence.getSequenceAsString().substring(0, startPosition) +
                            sequence +
                            storedSequence.getSequenceAsString().substring(startPosition + sequence.length())
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new GenBankFileEditorException("Cannot create a sequence", e);
        }
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

    private <S extends AbstractSequence<C>, C extends Compound> void modifyFeaturesLocationAfterCutting(
            GenbankSequenceParser<S, C> sequenceParser,
            S newSequence,
            int start,
            int seqLength
    ) {
        for (String k : sequenceParser.getFeatures().keySet()) {
            for (AbstractFeature<AbstractSequence<C>, C> f : sequenceParser.getFeatures().get(k)) {
                int featureStartPosition = f.getLocations().getStart().getPosition();
                int featureEndPosition = f.getLocations().getEnd().getPosition();
                if (featureEndPosition < start) {
                    newSequence.addFeature(f);
                }
                if (featureStartPosition < start) {
                    if (featureEndPosition > start && featureEndPosition < start + seqLength) {
                        f.setLocation(new SimpleLocation(featureStartPosition, start));
                        newSequence.addFeature(f);
                    }
                    if (featureEndPosition > start + seqLength) {
                        f.setLocation(new SimpleLocation(featureStartPosition, featureEndPosition - seqLength));
                        newSequence.addFeature(f);
                    }
                }
                if (featureStartPosition > start && featureStartPosition < start + seqLength) {
                    if (featureEndPosition > start + seqLength) {
                        f.setLocation(new SimpleLocation(start, featureEndPosition - seqLength));
                        newSequence.addFeature(f);
                    }
                }
                if (featureStartPosition > start + seqLength) {
                    f.setLocation(new SimpleLocation(featureStartPosition - seqLength, featureEndPosition - seqLength));
                    newSequence.addFeature(f);
                }
            }
        }
    }
}
