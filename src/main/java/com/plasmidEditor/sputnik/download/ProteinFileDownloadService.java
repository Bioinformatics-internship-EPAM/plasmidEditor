package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import com.plasmidEditor.sputnik.exceptions.GenBankNotFoundException;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.springframework.stereotype.Service;

@Service
public class ProteinFileDownloadService implements GenbankFileDownloadService<ProteinSequence> {
    @Override
    public ProteinSequence downloadFileAsSequence(String accession, String version) {
        try {
            GenBankDTO fileDTO = downloadFile(accession, version);
            return new ProteinSequence(fileDTO.getFile());
        } catch (GenBankNotFoundException | CompoundNotFoundException e) {
            if (version.equals("latest")) {
                throw new DownloadGenbankFileException(accession);
            } else {
                throw new DownloadGenbankFileException(accession, version);
            }
        }
        //аналогичный код в DNAFileDownloadService, отличие только в создаваемой Sequence
        //наверное что-то должно быть иначе, раз такое получилось
        //может вообще интерфейс с двумя реализациями не нужен?
        //похоже нам почти без разницы, protein или dna - загрузка файла одинаковая, кроме Sequence
    }
}
