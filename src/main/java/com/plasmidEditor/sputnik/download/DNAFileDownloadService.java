package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import com.plasmidEditor.sputnik.exceptions.GenBankNotFoundException;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.springframework.stereotype.Service;

@Service
public class DNAFileDownloadService implements GenbankFileDownloadService<DNASequence> {
    @Override
    public DNASequence downloadFileAsSequence(String accession, String version) {
        try {
            GenBankDTO fileDTO = downloadFile(accession, version);
            return new DNASequence(fileDTO.getFile());
        } catch (GenBankNotFoundException | CompoundNotFoundException e) {
            if (version.equals("latest")) {
                throw new DownloadGenbankFileException(accession);
            } else {
                throw new DownloadGenbankFileException(accession, version);
            }
        }
    }
}
