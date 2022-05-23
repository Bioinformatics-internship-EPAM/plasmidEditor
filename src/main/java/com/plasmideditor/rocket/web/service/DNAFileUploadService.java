package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.genbank.io.dna.GenBankDNAInputStreamReader;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankReaderException;
import com.plasmideditor.rocket.repositories.GenBankRepository;
import com.plasmideditor.rocket.web.exceptions.FileUploadException;
import com.plasmideditor.rocket.web.exceptions.FileAlreadyExistsException;
import com.plasmideditor.rocket.web.exceptions.SequenceValidationException;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.sequence.DNASequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class DNAFileUploadService implements FileUploadService<DNASequence> {

    private final GenBankRepository genBankRepository;

    @Autowired
    public DNAFileUploadService(GenBankRepository genBankRepository) {
        this.genBankRepository = genBankRepository;
    }

    @Override
    public void uploadFile(InputStream inputStream) throws FileUploadException, SequenceValidationException, FileAlreadyExistsException {
        List<DNASequence> sequenceList;
        FileUploadServiceUtils<DNASequence> serviceUtils = new FileUploadServiceUtils<>();
        String content;

        try {
            byte[] inputBytes = inputStream.readAllBytes();

            // If possible to read then the format is correct
            sequenceList = new GenBankDNAInputStreamReader().readSequence(new ByteArrayInputStream(inputBytes));
            content = new String(inputBytes);
        } catch (GenBankReaderException e) {
            throw new SequenceValidationException(e.getMessage(), e);
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage(), e);
        }

        serviceUtils.validateSequenceList(sequenceList);
        DNASequence sequence = sequenceList.get(0);
        String accession = sequence.getAccession().getID();
        int version = sequence.getAccession().getVersion();
        serviceUtils.insertSequence(genBankRepository, accession, version, content);
    }
}
