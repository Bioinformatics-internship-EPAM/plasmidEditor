package com.plasmidEditor.sputnik.controllers;

import com.plasmidEditor.sputnik.download.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class GenbankFileDownloadController {
    private final GenbankFileDownloadService dnaFileEditorService;
    private final GenbankFileDownloadService proteinFileEditorService;

    @Autowired
    public GenbankFileDownloadController(DNAFileDownloadService dnaFileEditorService,
                                         ProteinFileDownloadService proteinFileEditorService) {
        this.dnaFileEditorService = dnaFileEditorService;
        this.proteinFileEditorService = proteinFileEditorService;
    }

    @GetMapping(path = "/genbank/dna",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadDNA(@RequestParam String accession,
                                              @RequestParam(defaultValue = "latest") String version) {
        String dnaFile = dnaFileEditorService.downloadFileAsString(accession, version);
        return ResponseEntity.ok()
            .header("Download-Status", "File was successfully downloaded")
            .body(dnaFile);
    }

    @GetMapping(path = "/genbank/protein",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadProtein(@RequestParam String accession,
                                                  @RequestParam(defaultValue = "latest") String version) {
        String proteinFile = proteinFileEditorService.downloadFileAsString(accession, version);
        return ResponseEntity.ok()
            .header("Download-Status", "File was successfully downloaded")
            .body(proteinFile);
    }

    @GetMapping(path = "/genbank/dna/download",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadDNAToFile(@RequestParam String accession,
                                                    @RequestParam String path,
                                                    @RequestParam(defaultValue = "latest") String version) {
        dnaFileEditorService.downloadGenbakFileAndWriteToFile(accession, path, version);
        return ResponseEntity.ok()
                .header("Download-Status", "File was successfully downloaded")
                .build();
    }

    @GetMapping(path = "/genbank/protein/download",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadProteinToFile(@RequestParam String accession,
                                                        @RequestParam String path,
                                                        @RequestParam(defaultValue = "latest") String version) {
        proteinFileEditorService.downloadGenbakFileAndWriteToFile(accession, path, version);
        return ResponseEntity.ok()
                .header("Download-Status", "File was successfully downloaded")
                .build();
    }
}
