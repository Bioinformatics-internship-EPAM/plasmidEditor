package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import com.plasmidEditor.sputnik.exceptions.GenBankNotFoundException;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProteinFileDownloadService implements GenbankFileDownloadService<ProteinSequence> {
    @Autowired
    GenBankServiceImpl service;

    @Override
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
}
