package com.plasmideditor.rocket.web.controller;

import com.plasmideditor.rocket.web.service.DNAFileEditorService;
import com.plasmideditor.rocket.web.service.ProteinFileEditorService;
import com.plasmideditor.rocket.web.service.exceptions.FileEditorUploadException;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.plasmideditor.rocket.web.config.WebConstants.DNA_ENDPOINT;
import static com.plasmideditor.rocket.web.config.WebConstants.PROTEIN_ENDPOINT;

@Controller
public class FileUploadController {

    private final DNAFileEditorService dnaFileEditorService;
    private final ProteinFileEditorService proteinFileEditorService;

    @Autowired
    public FileUploadController(DNAFileEditorService dnaFileEditorService, ProteinFileEditorService proteinFileEditorService) {
        this.dnaFileEditorService = dnaFileEditorService;
        this.proteinFileEditorService = proteinFileEditorService;
    }

    @PostMapping(
            path = DNA_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> uploadDNAFile(@RequestParam("file") MultipartFile file) throws IOException, FileEditorUploadException, SequenceValidationException {
        dnaFileEditorService.uploadFile(file.getInputStream());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(
            path = PROTEIN_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> uploadProteinFile(@RequestParam("file") MultipartFile file) throws IOException, FileEditorUploadException, SequenceValidationException {
        proteinFileEditorService.uploadFile(file.getInputStream());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
