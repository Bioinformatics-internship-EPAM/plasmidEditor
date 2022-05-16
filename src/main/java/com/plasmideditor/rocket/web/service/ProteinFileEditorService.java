package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.genbank.io.exceptions.GenBankReaderException;
import com.plasmideditor.rocket.genbank.io.protein.GenBankProteinFileReader;
import com.plasmideditor.rocket.genbank.repository.GenBankRepository;
import com.plasmideditor.rocket.web.service.exceptions.FileEditorUploadException;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileAlreadyExists;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class ProteinFileEditorService implements FileEditorService<ProteinSequence> {

    private final GenBankRepository genBankRepository;

    @Autowired
    public ProteinFileEditorService(GenBankRepository genBankRepository) {
        this.genBankRepository = genBankRepository;
    }

    @Override
    public void uploadFile(InputStream inputFile) throws FileEditorUploadException, SequenceValidationException, GenBankFileAlreadyExists {
        List<ProteinSequence> sequenceList;
        FileEditorServiceUtils<ProteinSequence> serviceUtils = new FileEditorServiceUtils<>();
        File file = null;
        String content;

        try {
            // Write to tmp file to get accession and version
            file = serviceUtils.inputStreamToTmpFile(inputFile);
            content = new String(inputFile.readAllBytes());

            // If possible to read then the format is correct
            sequenceList = new GenBankProteinFileReader().readSequence(file.getAbsolutePath());
        } catch (GenBankReaderException e) {
            throw new SequenceValidationException(e.getMessage(), e);
        } catch (IOException e) {
            throw new FileEditorUploadException(e.getMessage(), e);
        } finally {
            if (file != null && !file.delete())
                log.error("Failed to delete tmp file " + file.getName());
        }

        serviceUtils.validateSequenceList(sequenceList);
        ProteinSequence sequence = sequenceList.get(0);
        String accession = sequence.getAccession().getID();
        int version = sequence.getAccession().getVersion();
        serviceUtils.insertSequence(genBankRepository, accession, version, content);
    }
}
