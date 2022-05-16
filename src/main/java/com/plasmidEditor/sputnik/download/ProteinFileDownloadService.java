package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.*;
import org.springframework.stereotype.Service;

@Service
public class ProteinFileDownloadService extends GenbankFileDownloadService<ProteinSequence> {
    @Override
    public ProteinSequence downloadGenbankSequence(String accession, String version) {
        final GenBankDTO fileDTO = downloadFile(accession, version);
        try {
            ProteinSequence sequence = new ProteinSequence(fileDTO.getFile());
            sequence.setAccession(new AccessionID(accession));
            return sequence;
        } catch (CompoundNotFoundException e) {
            throw new DownloadGenbankFileException(accession);
        }
    }
}
