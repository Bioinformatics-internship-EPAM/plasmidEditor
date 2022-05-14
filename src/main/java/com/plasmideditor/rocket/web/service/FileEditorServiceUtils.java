package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Slf4j
public class FileEditorServiceUtils<S extends AbstractSequence> {

    public File inputStreamToTmpFile(InputStream inputStream) throws IOException {
        File file = File.createTempFile("sequence", ".tmp");
        file.deleteOnExit();

        Files.copy(
                inputStream,
                file.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        IOUtils.closeQuietly(inputStream);

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
                        "Upload only one sequence per file."
                );
        }
    }

    public void insertSequence(S sequence) {
        String accession = sequence.getAccession().getID();
        int version = sequence.getAccession().getVersion();

//             if (genBankService.getByAccessionVersion(accession, version) == null) {
//                 genBankService.upload(accession, version, new String(multipartFile.getBytes()));
//             }

        log.info("Upload " + accession + "." + version);
    }
}
