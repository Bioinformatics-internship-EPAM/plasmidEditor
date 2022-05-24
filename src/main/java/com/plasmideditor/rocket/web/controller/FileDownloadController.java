package com.plasmideditor.rocket.web.controller;

import com.plasmideditor.rocket.GenBankData;
import org.springframework.beans.factory.annotation.Autowired;
import com.plasmideditor.rocket.web.service.FileDownloadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import static com.plasmideditor.rocket.web.configuration.ApiConstants.*;

@RestController
@RequestMapping(path = ROOT_ENDPOINT)
public class FileDownloadController {
    private final FileDownloadService fileDownloadService;

    @Autowired
    public FileDownloadController(FileDownloadService fileDownloadService) {
        this.fileDownloadService = fileDownloadService;
    }

    @GetMapping(path = DOWNLOAD_FILE_PATH,
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public GenBankData downloadFile(@RequestBody String accession,
                                    @RequestBody String version) {
        GenBankData genBankData = fileDownloadService.downloadFile(accession, version);
        return genBankData;
    }
}