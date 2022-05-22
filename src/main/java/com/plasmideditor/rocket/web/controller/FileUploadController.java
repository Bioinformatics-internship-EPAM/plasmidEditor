package com.plasmideditor.rocket.web.controller;

import com.plasmideditor.rocket.web.exceptions.FileEditorUploadException;
import com.plasmideditor.rocket.web.exceptions.GenBankFileAlreadyExistsException;
import com.plasmideditor.rocket.web.exceptions.SequenceValidationException;
import com.plasmideditor.rocket.web.service.DNAFileEditorService;
import com.plasmideditor.rocket.web.service.ProteinFileEditorService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.plasmideditor.rocket.web.configuration.ApiConstants.*;

@Controller
@RequestMapping(value = ROOT_ENDPOINT)
public class FileUploadController {

    @Autowired
    private DNAFileEditorService dnaFileEditorService;
    @Autowired
    private ProteinFileEditorService proteinFileEditorService;

    @PostMapping(
            path = DNA_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> uploadDNAFile(@RequestBody @NonNull MultipartFile file) throws
            IOException,
            FileEditorUploadException,
            SequenceValidationException,
            GenBankFileAlreadyExistsException
    {
        dnaFileEditorService.uploadFile(file.getInputStream());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(
            path = PROTEIN_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> uploadProteinFile(@RequestBody MultipartFile file) throws
            IOException,
            FileEditorUploadException,
            SequenceValidationException,
            GenBankFileAlreadyExistsException
    {
        proteinFileEditorService.uploadFile(file.getInputStream());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
