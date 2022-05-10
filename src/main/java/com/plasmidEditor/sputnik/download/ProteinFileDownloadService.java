package com.plasmidEditor.sputnik.download;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.springframework.web.multipart.MultipartFile;

public class ProteinFileDownloadService implements GenbankFileDownloadService<ProteinSequence> {

    @Override
    public void downloadFile(MultipartFile file) {  //throws DownloadGenbankFileException

    }
}
