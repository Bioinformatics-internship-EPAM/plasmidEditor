package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import com.plasmidEditor.sputnik.services.GenBankService;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;

public class DNAFileDownloadService implements GenbankFileDownloadService<DNASequence> {
    @Override
    public DNASequence downloadFile(String accession, String version) {
        GenBankService service = new GenBankServiceImpl();
        if (version.equals("latest")) {
            //search for latest version in database
            //version = ...
        }
        GenBankDTO fileDTO = service.get(accession, version);
        DNASequence dna;
        try {
            dna = new DNASequence(fileDTO.getFile());
        } catch (CompoundNotFoundException e) {
            throw new DownloadGenbankFileException(accession, version);
        }
        return dna;
    }
}
