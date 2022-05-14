package com.plasmideditor.rocket;

import com.plasmideditor.rocket.web.service.DNAFileEditorService;
import com.plasmideditor.rocket.web.service.FileEditorService;
import com.plasmideditor.rocket.web.service.ProteinFileEditorService;
import com.plasmideditor.rocket.web.service.exceptions.FileEditorUploadException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class FileEditorServiceTest {
    private final String TEST_DNA_FILE_PATH = "src/test/resources/BI431008.gb";
    private final String TEST_PROTEIN_FILE_PATH = "src/test/resources/3MJ8_A.gb";

    // Positive scenarios

    @Test
    public void uploadDNAFile() throws IOException, FileEditorUploadException {
        FileInputStream inputStream = getInputStreamFromFile(TEST_DNA_FILE_PATH);

        FileEditorService<DNASequence> fileEditorService = new DNAFileEditorService();
        fileEditorService.uploadFile(inputStream);
    }

    @Test
    public void uploadProteinFile() throws IOException, FileEditorUploadException {
        FileInputStream inputStream = getInputStreamFromFile(TEST_PROTEIN_FILE_PATH);

        FileEditorService<ProteinSequence> fileEditorService = new ProteinFileEditorService();
        fileEditorService.uploadFile(inputStream);
    }

    // DNA negative scenarios

    @Test
    public void uploadIncorrectDNAFile() {
        InputStream inputStream = getInputStreamFromContent("Incorrect DNA file");

        assertThrows(
                FileEditorUploadException.class,
                ()-> new DNAFileEditorService().uploadFile(inputStream),
                "File has invalid format."
        );
    }

    @Test
    public void uploadProteinInsteadDNAFile() throws IOException {
        FileInputStream inputStream = getInputStreamFromFile(TEST_PROTEIN_FILE_PATH);

        assertThrows(
                FileEditorUploadException.class,
                ()-> new DNAFileEditorService().uploadFile(inputStream),
                "File has invalid format."
        );
    }

    // Protein negative scenarios

    @Test
    public void uploadIncorrectProteinFile() {
        InputStream inputStream = getInputStreamFromContent("Incorrect Protein file");

        assertThrows(
                FileEditorUploadException.class,
                ()-> new ProteinFileEditorService().uploadFile(inputStream),
                "File has invalid format."
        );
    }

    @Test
    public void uploadDNAInsteadProteinFile() throws IOException {
        FileInputStream inputStream = getInputStreamFromFile(TEST_DNA_FILE_PATH);

        assertThrows(
                FileEditorUploadException.class,
                ()-> new ProteinFileEditorService().uploadFile(inputStream),
                "File has invalid format."
        );
    }

    private FileInputStream getInputStreamFromFile(String filename) throws FileNotFoundException {
        return new FileInputStream(new File(filename));
    }

    private InputStream getInputStreamFromContent(String content) {
        return new ByteArrayInputStream(content.getBytes());
    }
}
