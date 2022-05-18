package com.plasmidEditor.sputnik.uploadServices;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.biojava.nbio.core.sequence.template.AbstractSequence;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import com.plasmidEditor.sputnik.exceptions.GenBankNotFoundException;
import com.plasmidEditor.sputnik.exceptions.RepeatFileUploadingException;
import com.plasmidEditor.sputnik.exceptions.SequenceValidateException;

public class FileUploadServiceUtils<T extends AbstractSequence<?>> {
	public T validateSequence(Map<String, T> sequences) {
		switch(sequences.size()) {
			case 0:
				throw new SequenceValidateException("File has invalid format");
			case 1:
				return sequences.values().iterator().next();
			default:
				throw new SequenceValidateException("Upload more than one sequence in file");
		}
	}
	
	public void saveSequenceToDB(T sequence, File file, GenBankServiceImpl genBankServiceImpl) throws IllegalArgumentException {
		if (sequence instanceof T) {
			String accession = sequence.getAccession().getID();
			int version = sequence.getAccession().getVersion();
			String content = ReaderUtils.readStringFromFile(file.getAbsolutePath());
			try {
				genBankServiceImpl.get(accession, String.valueOf(version));
				throw new RepeatFileUploadingException("File with accession" + accession +" and version" + String.valueOf(version) + " already exists");
			} catch (GenBankNotFoundException e) {
				genBankServiceImpl.save(new GenBankDTO(accession, String.valueOf(version), content));
			}	
		}
		else {
			throw new IllegalArgumentException("Sequence has invalid type");
		}
	}

}
