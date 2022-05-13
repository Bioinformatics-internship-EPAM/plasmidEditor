package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.*;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.*;
import org.springframework.stereotype.Service;

@Service
public class DNAFileDownloadService extends GenbankFileDownloadService {
    @Override
    public void downloadGenbankFileAndWriteToFile(String accession, String savingPath, String version) {
        final GenBankDTO fileDTO = downloadFile(accession, version);
        try {
            DNASequence sequence = new DNASequence(fileDTO.getFile());
            sequence.setAccession(new AccessionID(accession));
            new FileDNAGenbankManager().writeSequence(savingPath, sequence);
        } catch (CompoundNotFoundException e) {
            throw new DownloadGenbankFileException(accession);
        }
    }
}
