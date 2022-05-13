package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.exceptions.*;
import com.plasmidEditor.sputnik.services.GenBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class GenbankFileDownloadService {
    @Autowired
    private GenBankService service;

    public GenBankDTO downloadFile(String accession, String version) {
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

    public String downloadFileAsString(String accession, String version) {
        return downloadFile(accession, version).getFile();
    }

    public abstract void downloadGenbankFileAndWriteToFile(String accession, String path, String version);
}
