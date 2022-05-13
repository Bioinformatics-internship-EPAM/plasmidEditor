package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.exceptions.*;
import com.plasmidEditor.sputnik.services.GenBankService;
import org.springframework.stereotype.Service;

@Service
public interface GenbankFileDownloadService {
    default GenBankDTO downloadFile(String accession, String version) {
        GenBankService service = getService();
        try {
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

    default String downloadFileAsString(String accession, String version) {
        GenBankDTO fileDTO = downloadFile(accession, version);
        return fileDTO.getFile();
    }

    void downloadGenbakFileAndWriteToFile(String accession, String path, String version);

    GenBankService getService();
}
