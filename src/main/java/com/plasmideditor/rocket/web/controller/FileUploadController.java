package com.plasmideditor.rocket.web.controller;

import com.plasmideditor.rocket.web.service.DNAFileUploadService;
import com.plasmideditor.rocket.web.service.ProteinFileUploadService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.plasmideditor.rocket.web.configuration.ApiConstants.*;

@Controller
@RequestMapping(value = ROOT_ENDPOINT)
public class FileUploadController {

    @Autowired
    private DNAFileUploadService dnaFileEditorService;
    @Autowired
    private ProteinFileUploadService proteinFileEditorService;

    @PostMapping(
            path = DNA_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadDNAFile(@RequestBody @NonNull MultipartFile file) throws IOException {
        dnaFileEditorService.uploadFile(file.getInputStream());
    }

    @PostMapping(
            path = PROTEIN_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadProteinFile(@RequestBody MultipartFile file) throws IOException {
        proteinFileEditorService.uploadFile(file.getInputStream());
    }
}
