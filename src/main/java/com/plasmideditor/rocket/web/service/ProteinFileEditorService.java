package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.database.repositories.GenBankRepository;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankReaderException;
import com.plasmideditor.rocket.genbank.io.protein.GenBankProteinInputStreamReader;
import com.plasmideditor.rocket.web.exceptions.FileEditorUploadException;
import com.plasmideditor.rocket.web.exceptions.GenBankFileAlreadyExistsException;
import com.plasmideditor.rocket.web.exceptions.SequenceValidationException;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
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
    public void uploadFile(InputStream inputStream) throws FileEditorUploadException, SequenceValidationException, GenBankFileAlreadyExistsException {
        List<ProteinSequence> sequenceList;
        FileEditorServiceUtils<ProteinSequence> serviceUtils = new FileEditorServiceUtils<>();
        String content;

        try {
            byte[] inputBytes = inputStream.readAllBytes();

            // If possible to read then the format is correct
            sequenceList = new GenBankProteinInputStreamReader().readSequence(new ByteArrayInputStream(inputBytes));
            content = new String(inputBytes);
        } catch (GenBankReaderException e) {
            throw new SequenceValidationException(e.getMessage(), e);
        } catch (IOException e) {
            throw new FileEditorUploadException(e.getMessage(), e);
        }

        serviceUtils.validateSequenceList(sequenceList);
        ProteinSequence sequence = sequenceList.get(0);
        String accession = sequence.getAccession().getID();
        int version = sequence.getAccession().getVersion();
        serviceUtils.insertSequence(genBankRepository, accession, version, content);
    }
}
