package com.plasmidEditor.sputnik.uploadServices;

import com.plasmidEditor.sputnik.exceptions.FileUploadException;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Service
public class ProteinFileUploadService implements FileUploadService<ProteinSequence> {

	private final GenBankServiceImpl genBankServiceImpl;
	
	@Autowired
	public ProteinFileUploadService(GenBankServiceImpl genBankServiceImpl) {
		this.genBankServiceImpl = genBankServiceImpl;
	}

	@Override
	public void uploadFile(InputStream inputStream) throws FileUploadException {
		ProteinSequence sequence;
		FileUploadServiceUtils<ProteinSequence> serviceUtils = new FileUploadServiceUtils<>();

        	try {
            		Map<String, ProteinSequence> sequences = GenbankReaderHelper.readGenbankProteinSequence(inputStream);
            		sequence = serviceUtils.validateSequence(sequences);
            		serviceUtils.saveSequenceToDB(sequence, inputStream, genBankServiceImpl);
        	} catch (Exception e) {
            		throw new FileUploadException(e.getMessage(), e);
        	}
		
    }

}
