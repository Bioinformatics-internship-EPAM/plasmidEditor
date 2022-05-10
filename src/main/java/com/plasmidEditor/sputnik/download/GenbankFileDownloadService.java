package com.plasmidEditor.sputnik.download;

import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.springframework.web.multipart.MultipartFile;

public interface GenbankFileDownloadService<T extends AbstractSequence<?>> {

    void downloadFile(MultipartFile file); //throws DownloadGenbankFileException

}
