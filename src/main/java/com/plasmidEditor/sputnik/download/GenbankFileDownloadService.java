package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import com.plasmidEditor.sputnik.exceptions.GenBankNotFoundException;
import com.plasmidEditor.sputnik.services.GenBankService;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
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

    default GenBankDTO downloadFile(String accession, String version) {
        try {
            GenBankService service = new GenBankServiceImpl();
            if (version.equals("latest")) {
                return service.getLatestVersion(accession);
            } else {
                return service.get(accession, version);
            }
        } catch (GenBankNotFoundException e) {
            if (version.equals("latest")) {
                throw new DownloadGenbankFileException(accession);
            } else {
                throw new DownloadGenbankFileException(accession, version);
            }
        }
    }
}
