package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.genbank.repository.GenBankRepository;
import com.plasmideditor.rocket.web.domains.GenBankEntity;
import com.plasmideditor.rocket.web.domains.request.ModificationRequest;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileEditorException;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileNotFound;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import com.plasmideditor.rocket.web.service.exceptions.UnknownSequenceType;
import com.plasmideditor.rocket.web.service.modifications.SequenceModification;
import lombok.extern.slf4j.Slf4j;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojava.bio.symbol.*;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.GenbankWriterHelper;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
public class EditService {
    private final String bpRegex = "[atcg]+";
    private final String aaRegex = "[arndceghilkmfpswyqvt]+";
    private final String UNKNOWN_SEQ_TYPE = "Sequence type is unknown";

    private final String AMINO_ACID = "aa";

    private final String BASE_PAIR = "bp";
    private final String BASE_PAIR_TYPE = "DNA";

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

    @Transactional
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
        updatedGenBank.setAccession(id);
        String newVersion = updateVersion(version);
        updatedGenBank.setVersion(newVersion);
        updatedGenBank.setFile(byteArrayOutputStream.toString());

        genBankRepository.save(updatedGenBank);
    }

    public Class getSequenceType(String file) throws UnknownSequenceType {
        String[] locusStringContent = file.split("\n")[0].split(" +");
        String lengthUnits = locusStringContent[3];
        String type = locusStringContent[4];
        if (lengthUnits.equalsIgnoreCase(AMINO_ACID)) {
            log.debug("Sequence type is protein");
            return ProteinSequence.class;
        } else if (lengthUnits.equalsIgnoreCase(BASE_PAIR) && type.equalsIgnoreCase(BASE_PAIR_TYPE)) {
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

    public AbstractSequence modifySequence(ModificationRequest request, SequenceModification service) throws GenBankFileEditorException, GenBankFileNotFound, UnknownSequenceType, SequenceValidationException {
        String fileContent = getFileFromDB(request.getFileId(), request.getFileVersion());
        Class sequenceType = getSequenceType(fileContent);
        validateSequence(request.getSequence(), sequenceType);
        BufferedReader br = new BufferedReader(new StringReader(fileContent));
        AbstractSequence newSequence = service.runModificationProcess(br, request.getStartPosition(), request.getSequence(), sequenceType);
        saveSequenceToDB(request.getFileId(), request.getFileVersion(), newSequence);
        return newSequence;
    }

    private String updateVersion(String prevVersion) {
        int version = Integer.parseInt(prevVersion);
        version += 1;
        return Integer.toString(version);
    }
}
