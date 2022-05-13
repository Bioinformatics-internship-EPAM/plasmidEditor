package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.*;
import com.plasmidEditor.sputnik.exceptions.*;
import com.plasmidEditor.sputnik.services.GenBankService;
import lombok.Getter;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.*;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DNAFileDownloadService implements GenbankFileDownloadService {
    @Getter
    @Autowired
    GenBankService service;

    @Override
    public void downloadGenbakFileAndWriteToFile(String accession, String path, String version) {
        final GenBankDTO fileDTO = downloadFile(accession, version);
        try {
            DNASequence sequence = new DNASequence(fileDTO.getFile());
            sequence.setAccession(new AccessionID(accession));
            new FileDNAGenbankManager().writeSequence(path, sequence);
        } catch (CompoundNotFoundException e) {
            throw new DownloadGenbankFileException(accession);
        }
    }
}
