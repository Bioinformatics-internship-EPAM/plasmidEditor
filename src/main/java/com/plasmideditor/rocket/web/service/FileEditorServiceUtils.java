package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.genbank.repository.GenBankRepository;
import com.plasmideditor.rocket.genbank.repository.domains.GenBankEntity;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileAlreadyExists;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FileEditorServiceUtils<S extends AbstractSequence> {

    public File inputStreamToTmpFile(InputStream inputStream) throws IOException {
        File file = File.createTempFile("sequence", ".tmp");
        file.deleteOnExit();

        Files.copy(
                inputStream,
                file.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        return file;
    }

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
            throws GenBankFileAlreadyExists {
        Optional<GenBankEntity> genBankFile = genBankRepository.findByAccessionAndVersion(accession, version);
        if (genBankFile.isPresent()) {
            String msg = String.format("GenBank file with accession %s and version %d already exists", accession, version);
            throw new GenBankFileAlreadyExists(msg);
        }

        GenBankEntity updatedGenBank = new GenBankEntity(accession, version, content);
        genBankRepository.save(updatedGenBank);
        log.info("Upload " + accession + "." + version);
    }
}
