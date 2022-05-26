package com.plasmideditor.rocket.web.service;

import org.springframework.stereotype.Service;
import com.plasmideditor.rocket.GenBankData;
import com.plasmideditor.rocket.services.*;
import com.plasmideditor.rocket.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class FileDownloadService {
    private final GenBankService genBankService;

    @Autowired
    public FileDownloadService(GenBankService genBankService) {
        this.genBankService = genBankService;
    }

    public GenBankData downloadFile(String accession, String version) {
        if (version == null) {
            return genBankService.getLatest(accession);
        }
        else {
            return genBankService.get(accession, version);
        }
    }

}