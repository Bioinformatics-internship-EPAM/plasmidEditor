package com.plasmideditor.rocket;

import com.plasmideditor.rocket.web.service.DNAFileEditorService;
import com.plasmideditor.rocket.web.service.FileEditorService;
import com.plasmideditor.rocket.web.service.ProteinFileEditorService;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class FileEditorServiceTest {
    private final String TEST_DNA_FILE_PATH = "src/test/resources/BI431008.gb";
    private final String TEST_PROTEIN_FILE_PATH = "src/test/resources/3MJ8_A.gb";

    // replace 10 character in sequence with 'f'
    private final String TEST_DNA_FILE_INVALID_CHARACTER_PATH = "src/test/resources/BI431008_invalid_character.gb";
    // replace 10 character in sequence with 'b'
    private final String TEST_PROTEIN_FILE_INVALID_CHARACTER_PATH = "src/test/resources/3MJ8_A_invalid_character.gb";

    @Test
    public void testDnaFileUploadSuccessfully() throws IOException {
        FileInputStream inputStream = getInputStreamFromFile(TEST_DNA_FILE_PATH);

        FileEditorService<DNASequence> fileEditorService = new DNAFileEditorService();
        assertDoesNotThrow(() -> fileEditorService.uploadFile(inputStream));
    }

    @Test
    public void testProteinFileUploadSuccessfully() throws IOException {
        FileInputStream inputStream = getInputStreamFromFile(TEST_PROTEIN_FILE_PATH);

        FileEditorService<ProteinSequence> fileEditorService = new ProteinFileEditorService();
        assertDoesNotThrow(() -> fileEditorService.uploadFile(inputStream));
    }

    @Test
    public void testUploadingIncorrectDnaFileFails() {
        InputStream inputStream = getInputStreamFromContent("Incorrect DNA file");

        assertThrows(
                SequenceValidationException.class,
                ()-> new DNAFileEditorService().uploadFile(inputStream)
        );
    }

    @Test
    public void testUploadingProteinInsteadDNAFileFails() throws IOException {
        FileInputStream inputStream = getInputStreamFromFile(TEST_PROTEIN_FILE_PATH);

        assertThrows(
                SequenceValidationException.class,
                ()-> new DNAFileEditorService().uploadFile(inputStream),
                "File has invalid format."
        );
    }

    @Test
    public void testUploadingIncorrectProteinFileFails() {
        InputStream inputStream = getInputStreamFromContent("Incorrect Protein file");

        assertThrows(
                SequenceValidationException.class,
                ()-> new ProteinFileEditorService().uploadFile(inputStream)
        );
    }

    @Test
    public void testDNAInsteadProteinFileUploadSuccessfully() throws IOException {
        FileInputStream inputStream = getInputStreamFromFile(TEST_DNA_FILE_PATH);

        assertDoesNotThrow(()-> new ProteinFileEditorService().uploadFile(inputStream));
    }

    @Test
    public void testUploadingDnaFileWithInvalidCharacterFails() {
        InputStream inputStream = getInputStreamFromContent(TEST_DNA_FILE_INVALID_CHARACTER_PATH);

        assertThrows(
                SequenceValidationException.class,
                ()-> new DNAFileEditorService().uploadFile(inputStream)
        );
    }

    @Test()
    public void testUploadingProteinFileWithInvalidCharacterFails() {
        InputStream inputStream = getInputStreamFromContent(TEST_PROTEIN_FILE_INVALID_CHARACTER_PATH);

        assertThrows(
                SequenceValidationException.class,
                ()-> new ProteinFileEditorService().uploadFile(inputStream)
        );
    }

    private FileInputStream getInputStreamFromFile(String filename) throws FileNotFoundException {
        return new FileInputStream(filename);
    }

    private InputStream getInputStreamFromContent(String content) {
        return new ByteArrayInputStream(content.getBytes());
    }
}
