package com.plasmidEditor.sputnik.controllers;

import com.plasmidEditor.sputnik.download.DNAFileDownloadService;
import com.plasmidEditor.sputnik.download.ProteinFileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class GenbankFileDownloadController {
    private final DNAFileDownloadService dnaFileDownloadService;
    private final ProteinFileDownloadService proteinFileDownloadService;

    @Autowired
    public GenbankFileDownloadController(DNAFileDownloadService dnaFileEditorService,
                                         ProteinFileDownloadService proteinFileEditorService) {
        this.dnaFileDownloadService = dnaFileEditorService;
        this.proteinFileDownloadService = proteinFileEditorService;
    }

    @PostMapping(path="/genbank/dna",
                 produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadDNAFile(@RequestParam("file") MultipartFile file) {
        try {
            String accession = "";
            String version = "";
            dnaFileDownloadService.downloadFile(accession, version);
            return ResponseEntity.ok("File was successfully downloaded");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fail to download file\n" + e.getMessage());
        }
    }

    @PostMapping(path="/genbank/protein",
                 produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadProteinFile(@RequestParam("file") MultipartFile file) {
        try {
            String accession = "";
            String version = "";
            proteinFileDownloadService.downloadFile(accession, version);
            return ResponseEntity.ok("File was successfully downloaded");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fail to download file\n" + e.getMessage());
        }
    }
}
