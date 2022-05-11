package com.plasmidEditor.sputnik.uploadServices;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.springframework.web.multipart.MultipartFile;

public class ProteinFileUploadService implements FileUploadService<ProteinSequence> {

	@Override
	public void upload(MultipartFile file) {
		// TODO Auto-generated method stub
		void init();

	void store(MultipartFile file);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();
	}

}
