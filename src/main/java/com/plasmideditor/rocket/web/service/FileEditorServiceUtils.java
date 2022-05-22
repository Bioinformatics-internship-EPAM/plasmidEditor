package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.database.entities.GenBankEntity;
import com.plasmideditor.rocket.database.repositories.GenBankRepository;
import com.plasmideditor.rocket.web.exceptions.GenBankFileAlreadyExistsException;
import com.plasmideditor.rocket.web.exceptions.SequenceValidationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FileEditorServiceUtils<S extends AbstractSequence> {

    public void validateSequenceList(@NonNull List<S> sequences) throws SequenceValidationException {
        switch (sequences.size()) {
            case 0:
                throw new SequenceValidationException("File has invalid format.");
            case 1:
                break;
            default:
                throw new SequenceValidationException("Upload " +
                        sequences.size() + " sequences at once. " +
                        "Please, upload only one sequence per file."
                );
        }
    }

    public void insertSequence(@NonNull GenBankRepository genBankRepository, String accession, int version, String content)
            throws GenBankFileAlreadyExistsException {
        Optional<GenBankEntity> genBankFile = genBankRepository.findByAccessionAndVersion(accession, String.valueOf(version));
        if (genBankFile.isPresent()) {
            String msg = String.format("GenBank file with accession %s and version %d already exists", accession, version);
            throw new GenBankFileAlreadyExistsException(msg);
        }

        GenBankEntity updatedGenBank = GenBankEntity.builder()
                .accession(accession)
                .version(String.valueOf(version))
                .file(content)
                .build();
        genBankRepository.save(updatedGenBank);
        log.info("Upload " + accession + "." + version);
    }
}
