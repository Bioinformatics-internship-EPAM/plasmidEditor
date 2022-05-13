package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.exceptions.*;
import com.plasmidEditor.sputnik.services.GenBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.plasmidEditor.sputnik.utils.Constants.DEFAULT_VERSION_VALUE;

@Service
public abstract class GenbankFileDownloadService {
    @Autowired
    private GenBankService service;

    public GenBankDTO downloadFile(String accession, String version) {
        try {
            if (version.equals(DEFAULT_VERSION_VALUE)) {
                return service.getLatestVersion(accession);
            }
            return service.get(accession, version);
        } catch (GenBankNotFoundException e) {
            if (version.equals(DEFAULT_VERSION_VALUE)) {
                throw new DownloadGenbankFileException(accession);
            }
            throw new DownloadGenbankFileException(accession, version);
        }
    }

    public String downloadFileAsString(String accession, String version) {
        return downloadFile(accession, version).getFile();
    }

    public abstract void downloadGenbankFileAndWriteToFile(String accession, String savingPath, String version);
}
