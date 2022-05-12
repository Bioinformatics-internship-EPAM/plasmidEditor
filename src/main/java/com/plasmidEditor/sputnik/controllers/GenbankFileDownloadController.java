package com.plasmidEditor.sputnik.controllers;

import com.plasmidEditor.sputnik.download.DNAFileDownloadService;
import com.plasmidEditor.sputnik.download.ProteinFileDownloadService;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import com.plasmidEditor.sputnik.exceptions.GenBankNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
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
    public ResponseEntity<DNASequence> downloadDNAFile(@RequestParam String accession,
                                                  @RequestParam(defaultValue = "latest") String version) {
        DNASequence dna = dnaFileDownloadService.downloadFileAsSequence(accession, version);
        return ResponseEntity.ok()
                .header("Condition","File was successfully downloaded")
                .body(dna);
    }

    @GetMapping(path="/genbank/protein",
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProteinSequence> downloadProteinFile(@RequestParam String accession,
                                                      @RequestParam(defaultValue = "latest") String version) {
        ProteinSequence protein = proteinFileDownloadService.downloadFileAsSequence(accession, version);
        return ResponseEntity.ok()
                .header("Condition","File was successfully downloaded")
                .body(protein);
    }

    @ExceptionHandler({DownloadGenbankFileException.class, GenBankNotFoundException.class})
    public ResponseEntity<String> handleException(RuntimeException e) {
        return ResponseEntity.badRequest().body("Fail to download file\n" + e.getMessage());
    }
}
