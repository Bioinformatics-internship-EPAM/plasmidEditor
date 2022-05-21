package com.plasmidEditor.sputnik.uploadServices;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.exceptions.*;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

import java.io.*;
import java.util.Map;

public abstract class FileUploadService<T extends AbstractSequence<?>> {
    public abstract void upload(InputStream inputStream) throws FileUploadException;

    protected void saveSequenceToDB(T sequence, InputStream inputStream) throws IllegalArgumentException, IOException, GenBankNotFoundException, RepeatFileUploadingException {
        GenBankServiceImpl genBankServiceImpl = new GenBankServiceImpl();
        String accession = sequence.getAccession().getID();
        String version = String.valueOf(sequence.getAccession().getVersion());
        if (genBankServiceImpl.isExists(accession, version)) {
            throw new RepeatFileUploadingException(accession, version);
        } else {
            String content = new String(inputStream.readAllBytes());
            genBankServiceImpl.save(new GenBankDTO(accession, version, content));
        }
    }

    protected T validateSequence(Map<String, T> sequences) {
        switch (sequences.size()) {
            case 0:
                throw new SequenceValidateException("File has invalid format");
            case 1:
                return sequences.values().iterator().next();
            default:
                throw new SequenceValidateException("Upload more than one sequence in file");
        }
    }
}
