package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.services.GenBankService;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import lombok.SneakyThrows;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.io.GenbankWriter;
import org.springframework.web.multipart.MultipartFile;

public class DNAFileDownloadService implements GenbankFileDownloadService<DNASequence> {
    @Override
    public DNASequence downloadFile(String accession, String version) {  //throws DownloadGenbankFileException
        GenBankService service = new GenBankServiceImpl();
        GenBankDTO fileDTO = service.get(accession, version);
        DNASequence dna = null;
        try {
            dna = new DNASequence(fileDTO.getFile());
        } catch (CompoundNotFoundException e) {
            e.printStackTrace();
        }
        return dna;
    }
}
