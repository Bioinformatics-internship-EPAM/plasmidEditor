package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.services.GenBankService;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.springframework.web.multipart.MultipartFile;

public class ProteinFileDownloadService implements GenbankFileDownloadService<ProteinSequence> {
    @Override
    public ProteinSequence downloadFile(String accession, String version) {  //throws DownloadGenbankFileException
        GenBankService service = new GenBankServiceImpl();
        GenBankDTO fileDTO = service.get(accession, version);
        ProteinSequence dna = null;
        try {
            dna = new ProteinSequence(fileDTO.getFile());
        } catch (CompoundNotFoundException e) {
            e.printStackTrace();
        }
        return dna;
    }
}
