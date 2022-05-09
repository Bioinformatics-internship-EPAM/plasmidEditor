package com.plasmideditor.rocket;

import com.plasmideditor.rocket.web.service.DNAFileEditorService;
import com.plasmideditor.rocket.web.service.FileEditorService;
import com.plasmideditor.rocket.web.service.ProteinFileEditorService;
import com.plasmideditor.rocket.web.service.exceptions.FileEditorUploadException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class FileEditorServiceTest {
    private final String TEST_DNA_FILE_PATH = "src/test/resources/BI431008.gb";
    private final String TEST_PROTEIN_FILE_PATH = "src/test/resources/3MJ8_A.gb";

    @Test
    public void uploadDNAFile() throws IOException, FileEditorUploadException {
        File file = new File(TEST_DNA_FILE_PATH);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                file.getName(),
                "text/plain",
                input.readAllBytes()
        );

        FileEditorService fileEditorService = new DNAFileEditorService();
        fileEditorService.uploadFile(multipartFile);
    }

    @Test
    public void uploadProteinFile() throws IOException, FileEditorUploadException {
        File file = new File(TEST_PROTEIN_FILE_PATH);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                file.getName(),
                "text/plain",
                input.readAllBytes()
        );

        FileEditorService fileEditorService = new ProteinFileEditorService();
        fileEditorService.uploadFile(multipartFile);
    }
}
