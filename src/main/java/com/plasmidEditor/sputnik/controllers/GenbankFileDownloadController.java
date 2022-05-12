package com.plasmidEditor.sputnik.controllers;

import com.plasmidEditor.sputnik.download.DNAFileDownloadService;
import com.plasmidEditor.sputnik.download.ProteinFileDownloadService;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/")
public class GenbankFileDownloadController {
    private final DNAFileDownloadService dnaFileDownloadService;
    private final ProteinFileDownloadService proteinFileDownloadService;

    @Autowired
    public GenbankFileDownloadController(DNAFileDownloadService dnaFileEditorService,
                                         ProteinFileDownloadService proteinFileEditorService) {
        this.dnaFileDownloadService = dnaFileEditorService;
        this.proteinFileDownloadService = proteinFileEditorService;
    }

    @GetMapping(path="/genbank/dna",
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadDNAFile(@RequestParam String accession,
                                             @RequestParam(defaultValue = "latest") String version) {
        System.out.println("accession: " + accession + " version: " + version);
        String dnaFile = dnaFileDownloadService.downloadFileAsString(accession, version);
        return ResponseEntity.ok() //status 200
                .header("Download-Status","File was successfully downloaded")
                .body(dnaFile);
    }

    @GetMapping(path="/genbank/protein",
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadProteinFile(@RequestParam String accession,
                                                 @RequestParam(defaultValue = "latest") String version) {
        String proteinFile = proteinFileDownloadService.downloadFileAsString(accession, version);
        return ResponseEntity.ok() //status 200
                .header("Download-Status","File was successfully downloaded")
                .body(proteinFile);
    }

    @ExceptionHandler(DownloadGenbankFileException.class)
    public ResponseEntity<String> handleException(DownloadGenbankFileException e) {
        return ResponseEntity.badRequest() //status 400
                .header("Download-Status","Fail to download file")
                .body("Reason: " + e.getMessage());
    }
}
