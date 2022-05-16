package com.plasmidEditor.sputnik.uploadServices;

import com.plasmidEditor.sputnik.FileDNAGenbankManager;
import com.plasmidEditor.sputnik.exceptions.FileUploadException;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.io.InputStream;

public class DNAFileUploadService implements FileUploadService<DNASequence> {

	private final GenBankServiceImpl genBankServiceImpl;
	
	@Autowired
	public DNAFileUploadService(GenBankServiceImpl genBankServiceImpl) {
		this.genBankServiceImpl = genBankServiceImpl;
	}

	@Override
	public void uploadFile(InputStream inputStream) throws FileUploadException {
		DNASequence sequence;

        	try {
            		File file = File.createTempFile("dna", ".tmp");
            		file.deleteOnExit();
            		inputStream.transferTo(file);
            		sequence = new FileDNAGenbankManager().readSequense(file.getAbsolutePath());
			if (sequense instanceof DNASequence) {
        			String accession = sequence.getAccession().getID();
        			int version = sequence.getAccession().getVersion();
				String content = new ReaderUtils().readStringFromFile(file.getAbsolutePath());
				genBankServiceImpl.save(new GenBankDTO(accession, String.valueOf(version), content));
			}
			else {
				throw new NullPointerException();
			}
        	} catch (Exception e) {
            		throw new FileUploadException(e.getMessage(), e);
        	}
		
    }

}
