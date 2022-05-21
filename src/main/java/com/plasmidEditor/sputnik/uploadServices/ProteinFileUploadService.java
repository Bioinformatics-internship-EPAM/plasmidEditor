package com.plasmidEditor.sputnik.uploadServices;

import com.plasmidEditor.sputnik.exceptions.FileUploadException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Service
public class ProteinFileUploadService extends FileUploadService<ProteinSequence> {
    @Override
    public void upload(InputStream inputStream) throws FileUploadException {
        try {
            Map<String, ProteinSequence> sequences = GenbankReaderHelper.readGenbankProteinSequence(inputStream);
            ProteinSequence sequence = validateSequence(sequences);
            saveSequenceToDB(sequence, inputStream);
        } catch (Exception e) {
            throw new FileUploadException(e.getMessage(), e);
        }
    }
}
