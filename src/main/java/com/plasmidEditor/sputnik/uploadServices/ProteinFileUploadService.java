package com.plasmidEditor.sputnik.uploadServices;

import com.plasmidEditor.sputnik.exceptions.FileUploadException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@Service
public class ProteinFileUploadService extends FileUploadService<ProteinSequence> {
    @Override
    public void upload(InputStream inputStream) throws FileUploadException {
        try {
        	byte[] cachedInputStream = inputStream.readAllBytes();
            Map<String, ProteinSequence> sequences = GenbankReaderHelper.readGenbankProteinSequence(new ByteArrayInputStream(cachedInputStream));
            ProteinSequence sequence = validateSequence(sequences);
            saveSequenceToDB(sequence, cachedInputStream);
        } catch (Exception e) {
            throw new FileUploadException(e.getMessage(), e);
        }
    }
}
