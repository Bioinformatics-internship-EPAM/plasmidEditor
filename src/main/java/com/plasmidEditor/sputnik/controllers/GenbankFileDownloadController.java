package com.plasmidEditor.sputnik.controllers;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.download.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.plasmidEditor.sputnik.utils.Constants.*;
import static com.plasmidEditor.sputnik.utils.RequestPath.*;

@RestController
@RequestMapping(path = GENBANK)
public class GenbankFileDownloadController {
    private final GenbankFileDownloadService genbankFileDownloadService;

    @Autowired
    public GenbankFileDownloadController(GenbankFileDownloadService genbankFileDownloadService) {
        this.genbankFileDownloadService = genbankFileDownloadService;
    }

    @GetMapping(path = DOWNLOAD,
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public byte[] downloadGenbankFile(@RequestParam String accession,
                                      @RequestParam(defaultValue = DEFAULT_VERSION_VALUE) String version) {
        GenBankDTO genBankDTO = genbankFileDownloadService.downloadFile(accession, version);
        return genbankFileDownloadService.convertGenbankDTOToByteArray(genBankDTO);
    }
}
