package com.plasmideditor.rocket.web.controller;

import com.plasmideditor.rocket.GenBankData;
import org.springframework.beans.factory.annotation.Autowired;
import com.plasmideditor.rocket.web.service.FileDownloadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import com.plasmideditor.rocket.exceptions.GenBankFileNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static com.plasmideditor.rocket.web.configuration.ApiConstants.*;

@RestController
@RequestMapping(path = ROOT_ENDPOINT)
public class FileDownloadController {
    private transient final FileDownloadService fileDownloadService;

    @Autowired
    public FileDownloadController(FileDownloadService fileDownloadService) {
        this.fileDownloadService = fileDownloadService;
    }

    @GetMapping(path = DOWNLOAD_FILE_PATH,
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public byte[] downloadFile(@RequestBody String accession,
                               @RequestBody String version) {
        try {
            GenBankData genBankData = fileDownloadService.downloadFile(accession, version);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(genBankData);
            byte[] response = baos.toByteArray();
            oos.close();
            baos.close();
            return response;
        } catch (GenBankFileNotFoundException | IOException exception) {
            return null;
        }
    }
}