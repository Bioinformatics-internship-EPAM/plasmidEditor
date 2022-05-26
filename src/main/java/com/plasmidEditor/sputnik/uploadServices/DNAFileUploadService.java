package com.plasmidEditor.sputnik.uploadServices;

import com.plasmidEditor.sputnik.exceptions.FileUploadException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Service
public class DNAFileUploadService extends FileUploadService<DNASequence> {
    @Override
    public void upload(InputStream inputStream) throws FileUploadException {
        try {
            Map<String, DNASequence> sequences = GenbankReaderHelper.readGenbankDNASequence(inputStream);
            DNASequence sequence = validateSequence(sequences);
            saveSequenceToDB(sequence, inputStream);
        } catch (Exception e) {
            throw new FileUploadException(e.getMessage(), e);
        }
    }
}
