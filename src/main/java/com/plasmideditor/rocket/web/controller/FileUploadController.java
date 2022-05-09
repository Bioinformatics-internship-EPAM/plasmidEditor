package com.plasmideditor.rocket.web.controller;

import com.plasmideditor.rocket.web.service.DNAFileEditorService;
import com.plasmideditor.rocket.web.service.ProteinFileEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {

    private final DNAFileEditorService dnaFileEditorService;
    private final ProteinFileEditorService proteinFileEditorService;

    @Autowired
    public FileUploadController(DNAFileEditorService dnaFileEditorService, ProteinFileEditorService proteinFileEditorService) {
        this.dnaFileEditorService = dnaFileEditorService;
        this.proteinFileEditorService = proteinFileEditorService;
    }

    @PostMapping(
            path="/genbank/dna",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> uploadDNAFile(@RequestParam("file") MultipartFile file) {
        try {
            dnaFileEditorService.uploadFile(file);
            return ResponseEntity.ok("Successfully upload file");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload file unexpected error. Reason: " + e.getMessage());
        }
    }

    @PostMapping(
            path="/genbank/protein",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> uploadProteinFile(@RequestParam("file") MultipartFile file) {
        try {
            proteinFileEditorService.uploadFile(file);
            return ResponseEntity.ok("Successfully upload file");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload file unexpected error. Reason: " + e.getMessage());
        }
    }
}
