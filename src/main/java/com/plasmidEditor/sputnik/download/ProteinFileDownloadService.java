package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import com.plasmidEditor.sputnik.services.GenBankService;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;

public class ProteinFileDownloadService implements GenbankFileDownloadService<ProteinSequence> {
    @Override
    public ProteinSequence downloadFile(String accession, String version) {
        GenBankService service = new GenBankServiceImpl();
        if (version == "latest") {
            //search for latest version in database
            //version = ...
        }
        GenBankDTO fileDTO = service.get(accession, version);
        ProteinSequence dna;
        try {
            dna = new ProteinSequence(fileDTO.getFile());
        } catch (CompoundNotFoundException e) {
            throw new DownloadGenbankFileException(accession, version);
        }
        return dna;
    }
}
