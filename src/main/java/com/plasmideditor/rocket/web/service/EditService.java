package com.plasmideditor.rocket.web.service;

import com.google.common.base.Strings;
import com.plasmideditor.rocket.entities.GenBankEntity;
import com.plasmideditor.rocket.repositories.GenBankRepository;
import com.plasmideditor.rocket.web.domains.request.ModificationRequest;
import com.plasmideditor.rocket.web.exceptions.*;
import com.plasmideditor.rocket.web.service.modifications.SequenceModification;
import com.plasmideditor.rocket.web.service.utils.ModificationFactory;
import com.plasmideditor.rocket.web.service.utils.Operations;
import lombok.extern.slf4j.Slf4j;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.GenbankWriterHelper;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.IOException;
import java.util.*;

import static com.plasmideditor.rocket.web.service.utils.CompoundNames.DNA_CLASS;
import static com.plasmideditor.rocket.web.service.utils.CompoundNames.PROTEIN_CLASS;

@Service
@Slf4j
@SuppressWarnings({"PMD.DataflowAnomalyAnalysis"})
public class EditService {
    private static final String UNKNOWN_SEQ_TYPE = "Sequence type is unknown";

    private static final String AMINO_ACID = "aa";

    private static final String BASE_PAIR = "bp";
    private static final String BASE_PAIR_TYPE = "DNA";

    private transient final GenBankRepository genBankRepository;

    @Autowired
    public EditService(GenBankRepository genBankRepository) {
        this.genBankRepository = genBankRepository;
    }

    public String getFileFromDB(String id, String version) {
        Optional<GenBankEntity> genBankFile = genBankRepository.findByAccessionAndVersion(id, version);
        if (genBankFile.isPresent()) {
            return genBankFile.get().getFile();
        }
        throw new GenBankFileNotFoundException();
    }

    @Transactional
    public <S extends AbstractSequence<C>, C extends Compound> void saveSequenceToDB(String id, String version, S newSequence) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            switch (newSequence.getClass().getSimpleName()) {
                case PROTEIN_CLASS:
                    GenbankWriterHelper.writeProteinSequence(byteArrayOutputStream, (Collection<ProteinSequence>) Collections.singleton(newSequence));
                    break;
                case DNA_CLASS:
                    GenbankWriterHelper.writeNucleotideSequence(byteArrayOutputStream, (Collection<DNASequence>) Collections.singleton(newSequence));
                    break;
            }
        } catch (Exception e) {
            throw new GenBankFileEditorException("Cannot write sequence", e);
        }
        // save to DB
        int newVersion = determineNewVersion(version, id);
        GenBankEntity updatedGenBank = GenBankEntity.builder()
                .accession(id)
                .version(String.valueOf(newVersion))
                .file(byteArrayOutputStream.toString())
                .build();

        genBankRepository.save(updatedGenBank);
    }

    public Class getSequenceType(String file) {
        String[] locusStringContent = file.split("\n")[0].split(" +");
        String lengthUnits = locusStringContent[3];
        String type = locusStringContent[4];
        switch (lengthUnits.toLowerCase(Locale.ROOT)) {
            case AMINO_ACID:
                log.debug("Sequence type is protein");
                return ProteinSequence.class;
            case BASE_PAIR:
                if (type.equalsIgnoreCase(BASE_PAIR_TYPE)) {
                    log.debug("Sequence type is DNA");
                    return DNASequence.class;
                }
            default:
                log.error(UNKNOWN_SEQ_TYPE);
                throw new UnknownSequenceTypeException(UNKNOWN_SEQ_TYPE);
        }
    }

    public <S extends AbstractSequence<C>, C extends Compound> void validateSequence(String sequence, Class<S> cls) {
        switch (cls.getSimpleName()) {
            case PROTEIN_CLASS:
                try {
                    ProteinTools.createProtein(sequence);
                } catch (IllegalSymbolException e) {
                    throw new SequenceValidationException("Illegal amino acid residues in sequence " + sequence);
                }
                break;
            case DNA_CLASS:
                try {
                    DNATools.createDNA(sequence);
                } catch (IllegalSymbolException e) {
                    throw new SequenceValidationException("Unknown nucleotide in sequence " + sequence);
                }
        }
        log.info("Sequence validation for {} is successful", sequence);
    }

    public <S extends AbstractSequence<C>, C extends Compound> S modifySequence(ModificationRequest request, Operations operation) {
        validateRequest(request);
        String fileContent = getFileFromDB(request.getFileId(), request.getFileVersion());
        Class sequenceType = getSequenceType(fileContent);
        validateSequence(request.getSequence(), sequenceType);
        try(BufferedReader br = new BufferedReader(new StringReader(fileContent))) {
            SequenceModification service = ModificationFactory.getOperation(operation, sequenceType);
            S newSequence = (S) service.runModificationProcess(br, request.getStartPosition(), request.getSequence(), sequenceType);
            saveSequenceToDB(request.getFileId(), request.getFileVersion(), newSequence);
            return newSequence;
        }
        catch (IOException e)
        {
            throw new FailedRequestHandleException("Failed to read content for modification");
        }
    }

    public void validateRequest(ModificationRequest request) {
        if (Strings.isNullOrEmpty(request.getFileId())) {
            throw new RequestBodyValidationException("Empty fileId in request body");
        }
        if (Strings.isNullOrEmpty(request.getFileVersion())) {
            throw new RequestBodyValidationException("Empty FileVersion in request body");
        }
        if (Strings.isNullOrEmpty(request.getSequence())) {
            throw new RequestBodyValidationException("Empty sequence in request body");
        }
        if (request.getStartPosition() == null) {
            throw new RequestBodyValidationException("Empty startPosition in request body");
        }
    }

    private int determineNewVersion(String prevVersion, String accessionId) {
        List<GenBankEntity> allVersions = genBankRepository.findGenBankEntitiesByAccession(accessionId);
        if (allVersions.isEmpty()) {
            return 1;
        }
        int maxVersion = getMaxVersion(prevVersion, allVersions);
        maxVersion += 1;
        return maxVersion;
    }

    private int getMaxVersion(String prevVersion, List<GenBankEntity> allVersions) {
        int maxVersion = Integer.parseInt(prevVersion);
        for (GenBankEntity entity : allVersions) {
            int entityVersion = Integer.parseInt(entity.getVersion());
            if (entityVersion > maxVersion) {
                maxVersion = entityVersion;
            }
        }
        return maxVersion;
    }
}
