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
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class FileEditorServiceTest {
    private final String TEST_DNA_FILE_PATH = "src/test/resources/BI431008.gb";
    private final String TEST_PROTEIN_FILE_PATH = "src/test/resources/3MJ8_A.gb";

    // Positive scenarios

    @Test
    public void uploadDNAFile() throws IOException, FileEditorUploadException {
        File file = new File(TEST_DNA_FILE_PATH);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = getMultipartFile(input, file.getName());

        FileEditorService fileEditorService = new DNAFileEditorService();
        fileEditorService.uploadFile(multipartFile);
    }

    @Test
    public void uploadProteinFile() throws IOException, FileEditorUploadException {
        File file = new File(TEST_PROTEIN_FILE_PATH);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = getMultipartFile(input, file.getName());

        FileEditorService fileEditorService = new ProteinFileEditorService();
        fileEditorService.uploadFile(multipartFile);
    }

    // DNA negative scenarios

    @Test
    public void uploadIncorrectDNAFile() {
        MultipartFile multipartFile = getMultipartFile("Incorrect DNA file");

        assertThrows(
                FileEditorUploadException.class,
                ()-> new DNAFileEditorService().uploadFile(multipartFile),
                "File has invalid format."
        );
    }

    @Test
    public void uploadProteinInsteadDNAFile() throws IOException {
        File file = new File(TEST_PROTEIN_FILE_PATH);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = getMultipartFile(input, file.getName());

        assertThrows(
                FileEditorUploadException.class,
                ()-> new DNAFileEditorService().uploadFile(multipartFile),
                "File has invalid format."
        );
    }

    // Protein negative scenarios

    @Test
    public void uploadIncorrectProteinFile() {
        MultipartFile multipartFile = getMultipartFile("Incorrect Protein file");

        assertThrows(
                FileEditorUploadException.class,
                ()-> new ProteinFileEditorService().uploadFile(multipartFile),
                "File has invalid format."
        );
    }

    @Test
    public void uploadDNAInsteadProteinFile() throws IOException {
        File file = new File(TEST_DNA_FILE_PATH);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = getMultipartFile(input, file.getName());

        assertThrows(
                FileEditorUploadException.class,
                ()-> new ProteinFileEditorService().uploadFile(multipartFile),
                "File has invalid format."
        );
    }

    private MultipartFile getMultipartFile(FileInputStream input, String filename) throws IOException {
        return new MockMultipartFile(
                "file",
                filename,
                "text/plain",
                input.readAllBytes()
        );
    }

    private MultipartFile getMultipartFile(String content) {
        return new MockMultipartFile(
                "file",
                "filename",
                "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
        );
    }
}
