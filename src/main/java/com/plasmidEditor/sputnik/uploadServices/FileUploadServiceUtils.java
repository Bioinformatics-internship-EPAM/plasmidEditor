package com.plasmidEditor.sputnik.uploadServices;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

import java.nio.file.Files;
import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import com.plasmidEditor.sputnik.utils.ReaderUtils;

public class FileUploadServiceUtils<T extends AbstractSequence> {
	public File writeToTmpFile(InputStream inputStream) throws IOException {
		File file = File.createTempFile("dna", ".tmp");
		file.deleteOnExit();
		Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        	IOUtils.closeQuietly(inputStream);
		return file;
	}
	
	public void saveSequenceToDB(T sequence, File file, GenBankServiceImpl genBankServiceImpl) throws IllegalArgumentException {
		if (sequence instanceof T) {
			String accession = sequence.getAccession().getID();
			int version = sequence.getAccession().getVersion();
			String content = ReaderUtils.readStringFromFile(file.getAbsolutePath());
			genBankServiceImpl.save(new GenBankDTO(accession, String.valueOf(version), content));
		}
		else {
			throw new IllegalArgumentException("Sequence has invalid type");
		}
	}

}
