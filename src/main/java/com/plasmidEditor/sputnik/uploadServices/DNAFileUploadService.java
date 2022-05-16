package com.plasmidEditor.sputnik.uploadServices;

import com.plasmidEditor.sputnik.FileDNAGenbankManager;
import com.plasmidEditor.sputnik.exceptions.FileUploadException;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;

import org.biojava.nbio.core.sequence.DNASequence;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;

public class DNAFileUploadService implements FileUploadService<DNASequence> {

	private final GenBankServiceImpl genBankServiceImpl;
	
	@Autowired
	public DNAFileUploadService(GenBankServiceImpl genBankServiceImpl) {
		this.genBankServiceImpl = genBankServiceImpl;
	}

	@Override
	public void upload(InputStream inputStream) throws FileUploadException {
		DNASequence sequence;
		FileUploadServiceUtils<DNASequence> serviceUtils = new FileUploadServiceUtils<>();

        try {
            	File file = serviceUtils.writeToTmpFile(inputStream);
            	sequence = new FileDNAGenbankManager().readSequence(file.getAbsolutePath());
            	serviceUtils.saveSequenceToDB(sequence, file, genBankServiceImpl);
        } catch (Exception e) {
            	throw new FileUploadException(e.getMessage(), e);
        }
		
    }

}
