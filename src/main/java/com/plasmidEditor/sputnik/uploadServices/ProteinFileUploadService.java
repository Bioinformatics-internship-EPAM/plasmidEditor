package com.plasmidEditor.sputnik.uploadServices;

import com.plasmidEditor.sputnik.FileProteinGenbankManager;
import com.plasmidEditor.sputnik.exceptions.FileUploadException;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;

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
            		File file = serviceUtils.writeToTmpFile(inputStream);
            		sequence = new FileProteinGenbankManager().readSequence(file.getAbsolutePath());
            		serviceUtils.saveSequenceToDB(sequence, file, genBankServiceImpl);
        	} catch (Exception e) {
            		throw new FileUploadException(e.getMessage(), e);
        	}
		
    }

}
