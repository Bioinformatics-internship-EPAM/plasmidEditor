package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.genbank.io.exceptions.GenBankReaderException;
import com.plasmideditor.rocket.genbank.io.protein.GenBankProteinFileReader;
import com.plasmideditor.rocket.web.service.exceptions.FileEditorUploadException;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.sequence.ProteinSequence;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class ProteinFileEditorService implements FileEditorService<ProteinSequence> {

    @Override
    public void uploadFile(InputStream inputFile) throws FileEditorUploadException, SequenceValidationException {
        List<ProteinSequence> sequenceList;
        FileEditorServiceUtils<ProteinSequence> serviceUtils = new FileEditorServiceUtils<>();

        try {
            // Write to tmp file to get accession and version
            File file = serviceUtils.inputStreamToTmpFile(inputFile);

            // If possible to read then the format is correct
            sequenceList = new GenBankProteinFileReader().readSequence(file.getAbsolutePath());
        } catch (GenBankReaderException e) {
            throw new SequenceValidationException(e.getMessage(), e);
        } catch (IOException e) {
            throw new FileEditorUploadException(e.getMessage(), e);
        }

        serviceUtils.validateSequenceList(sequenceList);
        ProteinSequence sequence = sequenceList.get(0);
        serviceUtils.insertSequence(sequence);
    }
}
