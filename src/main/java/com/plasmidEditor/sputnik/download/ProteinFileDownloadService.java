package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.*;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import com.plasmidEditor.sputnik.services.GenBankService;
import lombok.Getter;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProteinFileDownloadService implements GenbankFileDownloadService {
    @Getter
    @Autowired
    GenBankService service;

    @Override
    public void downloadGenbakFileAndWriteToFile(String accession, String path, String version) {
        GenBankDTO fileDTO = downloadFile(accession, version);
        try {
            ProteinSequence sequence = new ProteinSequence(fileDTO.getFile());
            sequence.setAccession(new AccessionID(accession));
            new FileProteinGenbankManager().writeSequence(path, sequence);
        } catch (CompoundNotFoundException e) {
            throw new DownloadGenbankFileException(accession);
        }
    }
}
