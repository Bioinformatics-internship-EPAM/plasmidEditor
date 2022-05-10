package com.plasmidEditor.sputnik.download;

import org.biojava.nbio.core.sequence.DNASequence;
import org.springframework.web.multipart.MultipartFile;

public class DNAFileDownloadService implements GenbankFileDownloadService<DNASequence> {

    @Override
    public void downloadFile(MultipartFile file) {  //throws DownloadGenbankFileException

    }
}
