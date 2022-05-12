package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.genbank.repository.GenBankRepository;
import com.plasmideditor.rocket.web.domains.GenBankEntity;
import com.plasmideditor.rocket.web.domains.request.ModificationRequest;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileEditorException;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileNotFound;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import com.plasmideditor.rocket.web.service.exceptions.UnknownSequenceType;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EditService {
    private final String bpRegex = "[atcg]+";
    private final String aaRegex = "[arndcqeghilkmfpsrwyv]+";

    private final String ADD_OPERATION = "ADD";
    private final String CUT_OPERATION = "CUT";
    
    private final String UNKNOWN_SEQ_TYPE = "Sequence type is unknown";
    private final String SEQ_LEN_OUT_OF_RANGE = "Modified sequence is out of range";
    private final String CAN_NOT_CREATE_SEQ = "Cannot create a sequence";

    private final GenBankRepository genBankRepository;

    public EditService(GenBankRepository genBankRepository) {
        this.genBankRepository = genBankRepository;
    }

    public String getFileFromDB(String id, String version) throws GenBankFileNotFound {
        Optional<GenBankEntity> genBankFile = genBankRepository.findByAccessionIdAndVersion(id, version);
        if (genBankFile.isPresent()) {
            return genBankFile.get().getFile();
        } else {
            throw new GenBankFileNotFound();
        }
    }

    public <S extends AbstractSequence<C>, C extends Compound> void saveSequenceToDB(String id, String version, S newSequence) throws GenBankFileEditorException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            if (newSequence.getClass() == ProteinSequence.class) {
                GenbankWriterHelper.writeProteinSequence(byteArrayOutputStream, (Collection<ProteinSequence>) Collections.singleton(newSequence));
            }
            if (newSequence.getClass() == DNASequence.class) {
                GenbankWriterHelper.writeNucleotideSequence(byteArrayOutputStream, (Collection<DNASequence>) Collections.singleton(newSequence));
            }
        } catch (Exception e) {
            throw new GenBankFileEditorException("Cannot write sequence", e);
        }
        // save to DB
        GenBankEntity updatedGenBank = new GenBankEntity();
        Optional<GenBankEntity> genBankFile = genBankRepository.findByAccessionIdAndVersion(id, version);
        if (genBankFile.isPresent()) {
            updatedGenBank.setAccession(id);
            updatedGenBank.setVersion("");
            updatedGenBank.setFile(byteArrayOutputStream.toString());
        } else {
            log.warn("Initial file was deleted during work with sequence");
            // what should we do here?

        }

        genBankRepository.save(updatedGenBank);
    }

    public Class getSequenceType(String file) throws UnknownSequenceType {
        String type = file.split("\n")[0].split(" +")[3];
        if (type.equalsIgnoreCase("aa")) {
            log.debug("Sequence type is protein");
            return ProteinSequence.class;
        } else if (type.equalsIgnoreCase("bp")) {
            log.debug("Sequence type is DNA");
            return DNASequence.class;
        } else {
            log.error(UNKNOWN_SEQ_TYPE);
            throw new UnknownSequenceType(UNKNOWN_SEQ_TYPE);
        }
    }

    public <S extends AbstractSequence<C>, C extends Compound> void validateSequence(String sequence, Class<S> cls) throws SequenceValidationException {
        if (cls == ProteinSequence.class && !sequence.toLowerCase().matches(aaRegex)) {
            throw new SequenceValidationException("Illegal amino acid residues in sequence " + sequence);
        }
        if (cls == DNASequence.class && !sequence.toLowerCase().matches(bpRegex)) {
            throw new SequenceValidationException("Illegal nucleotide base pair in sequence " + sequence);
        }
        log.info("Sequence validation is successful");
    }

    public AbstractSequence cutGenBankFile(ModificationRequest request) throws GenBankFileEditorException, GenBankFileNotFound, UnknownSequenceType {
        String fileContent = getFileFromDB(request.getFileId(), request.getFileVersion());
        Class sequenceType = getSequenceType(fileContent);
        BufferedReader br = new BufferedReader(new StringReader(fileContent));
        AbstractSequence newSequence = cut(br, request.getStartPosition(), request.getSequence(), sequenceType);
        saveSequenceToDB(request.getFileId(), request.getFileVersion(), newSequence);
        return newSequence;
    }

    public AbstractSequence modifyGenBankFile(ModificationRequest request) throws GenBankFileEditorException, GenBankFileNotFound, UnknownSequenceType, SequenceValidationException {
        String fileContent = getFileFromDB(request.getFileId(), request.getFileVersion());
        Class sequenceType = getSequenceType(fileContent);
        validateSequence(request.getSequence(), sequenceType);
        BufferedReader br = new BufferedReader(new StringReader(fileContent));
        AbstractSequence newSequence = modify(br, request.getStartPosition(), request.getSequence(), sequenceType);
        saveSequenceToDB(request.getFileId(), request.getFileVersion(), newSequence);
        return newSequence;
    }

    public AbstractSequence addGenBankFile(ModificationRequest request) throws GenBankFileEditorException, GenBankFileNotFound, UnknownSequenceType, SequenceValidationException {
        String fileContent = getFileFromDB(request.getFileId(), request.getFileVersion());
        Class sequenceType = getSequenceType(fileContent);
        validateSequence(request.getSequence(), sequenceType);
        BufferedReader br = new BufferedReader(new StringReader(fileContent));
        AbstractSequence newSequence = add(br, request.getStartPosition(), request.getSequence(), sequenceType);
        saveSequenceToDB(request.getFileId(), request.getFileVersion(), newSequence);
        return newSequence;
    }

    private <S extends AbstractSequence<C>, C extends Compound> S cut(
            BufferedReader br,
            int startPosition,
            String sequence,
            Class<S> cls
    ) throws GenBankFileEditorException {
        GenbankSequenceParser<S, C> sequenceParser = new GenbankSequenceParser<>();
        S storedSequence = createSequence(cls, sequenceParser.getSequence(br, 0));

        if (!storedSequence.getSequenceAsString().toLowerCase().startsWith(sequence.toLowerCase(), startPosition)) {
            log.error("Illegal start position or sequence to delete: {}, {}", startPosition, sequence);
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
        S storedSequence = createSequence(cls, sequenceParser.getSequence(br, 0));

        if (startPosition + sequence.length() > storedSequence.getLength()) {
            log.error(SEQ_LEN_OUT_OF_RANGE);
            throw new GenBankFileEditorException(SEQ_LEN_OUT_OF_RANGE);
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
        S storedSequence = createSequence(cls, sequenceParser.getSequence(br, 0));

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
            throw new GenBankFileEditorException(CAN_NOT_CREATE_SEQ, e);
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
            throw new GenBankFileEditorException(CAN_NOT_CREATE_SEQ, e);
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
            throw new GenBankFileEditorException(CAN_NOT_CREATE_SEQ, e);
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
                    log.debug("Start features modification for Cut operation");
                    updatePositionAfterCutOperation(newSequence, start, seqLength, f, featureStartPosition, featureEndPosition);
                } else if (operation.equals(ADD_OPERATION)) {
                    log.debug("Start features modification for Add operation");
                    updatePositionAfterAddOperation(newSequence, start, seqLength, f, featureStartPosition, featureEndPosition);
                } else {
                    throw new GenBankFileEditorException("Unknown modification operation");
                }
            }
        }
    }

    private <S extends AbstractSequence<C>, C extends Compound> void updatePositionAfterAddOperation(S newSequence, int start, int seqLength, AbstractFeature<AbstractSequence<C>, C> f, int featureStartPosition, int featureEndPosition) {
        if (featureStartPosition <= start) {
            f.setLocation(new SimpleLocation(featureStartPosition, featureEndPosition + seqLength));
            newSequence.addFeature(f);
        }
        if (featureStartPosition > start) {
            f.setLocation(new SimpleLocation(featureStartPosition + seqLength, featureEndPosition + seqLength));
            newSequence.addFeature(f);
        }
    }

    private <S extends AbstractSequence<C>, C extends Compound> void updatePositionAfterCutOperation(S newSequence, int start, int seqLength, AbstractFeature<AbstractSequence<C>, C> f, int featureStartPosition, int featureEndPosition) {
        if (featureStartPosition < start) {
            if (isFeaturePositionBetweenSequenceStartAndEnd(start, seqLength, featureEndPosition)) {
                f.setLocation(new SimpleLocation(featureStartPosition, start));
                newSequence.addFeature(f);
            }
            if (isFeaturePositionAfterSequenceEnd(start, seqLength, featureEndPosition)) {
                f.setLocation(new SimpleLocation(featureStartPosition, featureEndPosition - seqLength));
                newSequence.addFeature(f);
            }
        }
        if (isFeaturePositionBetweenSequenceStartAndEnd(start, seqLength, featureStartPosition)) {
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

    private boolean isFeaturePositionBetweenSequenceStartAndEnd(int start, int seqLength, int featurePosition) {
        return featurePosition >= start && featurePosition <= start + seqLength;
    }

    private <S extends AbstractSequence<C>, C extends Compound> S createSequence(Class<S> cls, String sequenceParser) throws GenBankFileEditorException {
        S storedSequence;
        try {
            storedSequence = cls.getConstructor(String.class).newInstance(sequenceParser);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new GenBankFileEditorException(CAN_NOT_CREATE_SEQ, e);
        }
        return storedSequence;
    }
}
