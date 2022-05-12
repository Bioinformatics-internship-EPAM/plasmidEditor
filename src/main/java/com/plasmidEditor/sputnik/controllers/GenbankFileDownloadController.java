package com.plasmidEditor.sputnik.controllers;

import com.plasmidEditor.sputnik.download.DNAFileDownloadService;
import com.plasmidEditor.sputnik.download.ProteinFileDownloadService;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import com.plasmidEditor.sputnik.exceptions.GenBankNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(path="/genbank/dna",
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadDNAFile(@RequestParam String accession,
                                                  @RequestParam(defaultValue = "latest") String version) {
        dnaFileDownloadService.downloadFile(accession, version);

        return ResponseEntity.ok("File was successfully downloaded");
    }

    @GetMapping(path="/genbank/protein",
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> downloadProteinFile(@RequestParam String accession,
                                                      @RequestParam(defaultValue = "latest") String version) {
        proteinFileDownloadService.downloadFile(accession, version);

        return ResponseEntity.ok("File was successfully downloaded");
    }

    //убрать отсюда GenBankNotFoundException.class?
    //и в downloadFile() интерфейса GenbankFileDownloadService обрабатывать его и бросать DownloadGenbankFileException?
    @ExceptionHandler({DownloadGenbankFileException.class, GenBankNotFoundException.class})
    public ResponseEntity<?> handleException(DownloadGenbankFileException e) {
        return ResponseEntity.badRequest().body("Fail to download file\n" + e.getMessage());
    }
}
