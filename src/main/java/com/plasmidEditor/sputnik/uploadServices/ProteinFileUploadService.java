package com.plasmidEditor.sputnik.uploadServices;

import com.plasmidEditor.sputnik.exceptions.FileUploadException;
import com.plasmidEditor.sputnik.services.GenBankService;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Service
public class ProteinFileUploadService implements FileUploadService<ProteinSequence> {

	private final GenBankService genBankService;
	
	@Autowired
	public ProteinFileUploadService(GenBankService genBankService) {
		this.genBankService = genBankService;
	}

	@Override
	public void uploadFile(InputStream inputStream) throws FileUploadException {
		ProteinSequence sequence;
		FileUploadServiceUtils<ProteinSequence> serviceUtils = new FileUploadServiceUtils<>();

        	try {
            		Map<String, ProteinSequence> sequences = GenbankReaderHelper.readGenbankProteinSequence(inputStream);
            		sequence = serviceUtils.validateSequence(sequences);
            		serviceUtils.saveSequenceToDB(sequence, inputStream, genBankService);
        	} catch (Exception e) {
            		throw new FileUploadException(e.getMessage(), e);
        	}
		
    }

}
