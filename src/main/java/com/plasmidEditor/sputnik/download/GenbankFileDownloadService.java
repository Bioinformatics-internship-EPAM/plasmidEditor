package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.exceptions.*;
import com.plasmidEditor.sputnik.services.GenBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

import static com.plasmidEditor.sputnik.utils.Constants.*;

@Service
public class GenbankFileDownloadService {
    private transient final GenBankService service;

    @Autowired
    public GenbankFileDownloadService(GenBankService service) {
        this.service = service;
    }

    public GenBankDTO downloadFile(String accession, String version) {
        try {
            if (version.equals(DEFAULT_VERSION_VALUE)) {
                return service.getLatestVersion(accession);
            }
            return service.get(accession, version);
        } catch (GenBankNotFoundException e) {
            if (version.equals(DEFAULT_VERSION_VALUE)) {
                throw new DownloadGenbankFileException(accession);
            }
            throw new DownloadGenbankFileException(accession, version);
        }
    }

    public byte[] convertGenbankDTOToByteArray(GenBankDTO genBankDTO) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(genBankDTO);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new DownloadGenbankFileException(genBankDTO.getAccession());
        }
    }
}
