package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import com.plasmidEditor.sputnik.exceptions.GenBankNotFoundException;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

public interface GenbankFileDownloadService<T extends AbstractSequence<?>> {

    default String downloadFileAsString(String accession, String version) {
        try {
            GenBankDTO fileDTO = downloadFile(accession, version);
            return fileDTO.getFile();
        } catch (GenBankNotFoundException e) {
            if (version.equals("latest")) {
                throw new DownloadGenbankFileException(accession);
            } else {
                throw new DownloadGenbankFileException(accession, version);
            }
        }
    }

    GenBankDTO downloadFile(String accession, String version);
}
