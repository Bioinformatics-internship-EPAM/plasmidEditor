package com.plasmidEditor.sputnik.controllers;

import com.plasmidEditor.sputnik.download.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.plasmidEditor.sputnik.utils.RequestPath.*;

@RestController(GENBANK)
public class GenbankFileDownloadController {
    private final GenbankFileDownloadService dnaFileDownloadService;
    private final GenbankFileDownloadService proteinFileDownloadService;

    @Autowired
    public GenbankFileDownloadController(DNAFileDownloadService dnaFileDownloadService,
                                         ProteinFileDownloadService proteinFileDownloadService) {
        this.dnaFileDownloadService = dnaFileDownloadService;
        this.proteinFileDownloadService = proteinFileDownloadService;
    }

    @GetMapping(path = DNA,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadDNA(@RequestParam String accession,
                                              @RequestParam(defaultValue = "latest") String version) {
        String dnaFile = dnaFileDownloadService.downloadFileAsString(accession, version);
        return ResponseEntity.ok()
            .header("Download-Status", "File was successfully downloaded")
            .body(dnaFile);
    }

    @GetMapping(path = PROTEIN,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadProtein(@RequestParam String accession,
                                                  @RequestParam(defaultValue = "latest") String version) {
        String proteinFile = proteinFileDownloadService.downloadFileAsString(accession, version);
        return ResponseEntity.ok()
            .header("Download-Status", "File was successfully downloaded")
            .body(proteinFile);
    }

    @GetMapping(path = DNA_DOWNLOAD,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadDNAToFile(@RequestParam String accession,
                                                    @RequestParam String savingPath,
                                                    @RequestParam(defaultValue = "latest") String version) {
        dnaFileDownloadService.downloadGenbakFileAndWriteToFile(accession, savingPath, version);
        return ResponseEntity.ok()
                .header("Download-Status", "File was successfully downloaded")
                .build();
    }

    @GetMapping(path = PROTEIN_DOWNLOAD,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadProteinToFile(@RequestParam String accession,
                                                        @RequestParam String savingPath,
                                                        @RequestParam(defaultValue = "latest") String version) {
        proteinFileDownloadService.downloadGenbakFileAndWriteToFile(accession, savingPath, version);
        return ResponseEntity.ok()
                .header("Download-Status", "File was successfully downloaded")
                .build();
    }
}
