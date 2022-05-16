package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.*;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.*;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class DNAFileDownloadService extends GenbankFileDownloadService<DNASequence> {
    @Override
    public DNASequence downloadGenbankSequence(String accession, String version) {
        final GenBankDTO fileDTO = downloadFile(accession, version);
        try {
            DNASequence sequence = new DNASequence(fileDTO.getFile());
            sequence.setAccession(new AccessionID(accession));
            return sequence;
        } catch (CompoundNotFoundException e) {
            throw new DownloadGenbankFileException(accession);
        }
    }
}
