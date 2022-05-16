package com.plasmidEditor.sputnik.uploadServices;

import com.plasmidEditor.sputnik.FileProteinGenbankManager;
import com.plasmidEditor.sputnik.exceptions.FileUploadException;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
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

        	try {
            		File file = File.createTempFile("Protein", ".tmp");
            		file.deleteOnExit();
            		inputStream.transferTo(file);
            		sequence = new FileProteinGenbankManager().readSequense(file.getAbsolutePath());
			if (sequenсe instanceof ProteinSequence) {
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
