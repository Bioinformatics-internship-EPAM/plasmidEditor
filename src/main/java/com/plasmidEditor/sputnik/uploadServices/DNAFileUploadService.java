package com.plasmidEditor.sputnik.uploadServices;

import com.plasmidEditor.sputnik.exceptions.FileUploadException;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Service
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
            		Map<String, DNASequence> sequences = GenbankReaderHelper.readGenbankDNASequence(inputStream);
            		sequence = serviceUtils.validateSequence(sequences);
            		serviceUtils.saveSequenceToDB(sequence, inputStream, genBankServiceImpl);
        	} catch (Exception e) {
            		throw new FileUploadException(e.getMessage(), e);
        	}
		
    }

}
