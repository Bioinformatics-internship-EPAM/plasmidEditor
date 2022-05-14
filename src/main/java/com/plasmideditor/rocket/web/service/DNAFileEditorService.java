package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.genbank.io.dna.GenBankDNAFileReader;
import com.plasmideditor.rocket.web.service.exceptions.FileEditorUploadException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

@Service
public class DNAFileEditorService implements FileEditorService<DNASequence> {

    @Override
    public void uploadFile(InputStream inputFile) throws FileEditorUploadException {
        List<DNASequence> sequenceList;
        FileEditorServiceUtils<DNASequence> serviceUtils = new FileEditorServiceUtils<>();

        try {
            // Write to tmp file to get accession and version
            File file = serviceUtils.inputStreamToTmpFile(inputFile);

            // If possible to read then the format is correct
            sequenceList = new GenBankDNAFileReader().readSequence(file.getAbsolutePath());
            serviceUtils.validateSequenceList(sequenceList);
        } catch (Exception e) {
            throw new FileEditorUploadException(e.getMessage(), e);
        }

        DNASequence sequence = sequenceList.get(0);
        serviceUtils.insertSequence(sequence);
    }
}
