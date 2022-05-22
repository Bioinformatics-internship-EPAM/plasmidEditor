package com.plasmideditor.rocket.service;

import com.plasmideditor.rocket.database.entities.GenBankEntity;
import com.plasmideditor.rocket.database.repositories.GenBankRepository;
import com.plasmideditor.rocket.web.exceptions.FileAlreadyExistsException;
import com.plasmideditor.rocket.web.exceptions.SequenceValidationException;
import com.plasmideditor.rocket.web.service.DNAFileUploadService;
import com.plasmideditor.rocket.web.service.FileUploadService;
import com.plasmideditor.rocket.web.service.ProteinFileUploadService;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class FileUploadServiceTest {
    private static final String TEST_DNA_ACCESSION = "BI431008";
    private static final String TEST_PROTEIN_ACCESSION = "3MJ8_A";
    private static final int TEST_DNA_VERSION = 1;
    private static final int TEST_PROTEIN_VERSION = 0;

    private static final String TEST_DNA_FILE_PATH = "src/test/resources/BI431008.gb";
    private static final String TEST_PROTEIN_FILE_PATH = "src/test/resources/3MJ8_A.gb";

    // replace 10 character in sequence with 'f'
    private static final String TEST_DNA_FILE_INVALID_CHARACTER_PATH = "src/test/resources/BI431008_invalid_character.gb";
    // replace 10 character in sequence with 'b'
    private static final String TEST_PROTEIN_FILE_INVALID_CHARACTER_PATH = "src/test/resources/3MJ8_A_invalid_character.gb";

    private static GenBankRepository mockGenBankRepository;

    @BeforeAll
    public static void initAll() {
        mockGenBankRepository = Mockito.mock(GenBankRepository.class);
    }

    @BeforeEach
    public void init() {
        Mockito.when(mockGenBankRepository
                .findByAccessionAndVersion(TEST_DNA_ACCESSION, String.valueOf(TEST_DNA_VERSION)))
                .thenReturn(Optional.empty());
        Mockito.when(mockGenBankRepository
                .findByAccessionAndVersion(TEST_PROTEIN_ACCESSION, String.valueOf(TEST_PROTEIN_VERSION)))
                .thenReturn(Optional.empty());
    }


    @Test
    public void testSuccessfullyUploadDnaFile() throws IOException {
        FileInputStream inputStream = getInputStreamFromFile(TEST_DNA_FILE_PATH);

        FileUploadService<DNASequence> fileEditorService = new DNAFileUploadService(mockGenBankRepository);
        assertDoesNotThrow(() -> fileEditorService.uploadFile(inputStream));
    }

    @Test
    public void testSuccessfullyUploadProteinFile() throws IOException {
        FileInputStream inputStream = getInputStreamFromFile(TEST_PROTEIN_FILE_PATH);

        FileUploadService<ProteinSequence> fileEditorService = new ProteinFileUploadService(mockGenBankRepository);
        assertDoesNotThrow(() -> fileEditorService.uploadFile(inputStream));
    }

    @Test
    public void testUploadingIncorrectDnaFileFails() {
        InputStream inputStream = getInputStreamFromContent("Incorrect DNA file");

        assertThrows(
                SequenceValidationException.class,
                ()-> new DNAFileUploadService(mockGenBankRepository).uploadFile(inputStream)
        );
    }

    @Test
    public void testUploadingProteinInsteadDNAFileFails() throws IOException {
        FileInputStream inputStream = getInputStreamFromFile(TEST_PROTEIN_FILE_PATH);

        assertThrows(
                SequenceValidationException.class,
                ()-> new DNAFileUploadService(mockGenBankRepository).uploadFile(inputStream),
                "File has invalid format."
        );
    }

    @Test
    public void testUploadingIncorrectProteinFileFails() {
        InputStream inputStream = getInputStreamFromContent("Incorrect Protein file");

        assertThrows(
                SequenceValidationException.class,
                ()-> new ProteinFileUploadService(mockGenBankRepository).uploadFile(inputStream)
        );
    }

    @Test
    public void testDNAInsteadProteinFileUploadSuccessfully() throws IOException {
        FileInputStream inputStream = getInputStreamFromFile(TEST_DNA_FILE_PATH);

        assertDoesNotThrow(()-> new ProteinFileUploadService(mockGenBankRepository).uploadFile(inputStream));
    }

    @Test
    public void testUploadingDnaFileWithInvalidCharacterFails() {
        InputStream inputStream = getInputStreamFromContent(TEST_DNA_FILE_INVALID_CHARACTER_PATH);

        assertThrows(
                SequenceValidationException.class,
                ()-> new DNAFileUploadService(mockGenBankRepository).uploadFile(inputStream)
        );
    }

    @Test()
    public void testUploadingProteinFileWithInvalidCharacterFails() {
        InputStream inputStream = getInputStreamFromContent(TEST_PROTEIN_FILE_INVALID_CHARACTER_PATH);

        assertThrows(
                SequenceValidationException.class,
                ()-> new ProteinFileUploadService(mockGenBankRepository).uploadFile(inputStream)
        );
    }

    @Test
    public void testUploadProteinFileWithExistingAccessionAndVersion() throws FileNotFoundException {
        GenBankEntity proteinEntity = GenBankEntity.builder()
                .accession(TEST_PROTEIN_ACCESSION)
                .version(String.valueOf(TEST_PROTEIN_VERSION))
                .build();
        Mockito.when(mockGenBankRepository
                .findByAccessionAndVersion(TEST_PROTEIN_ACCESSION, String.valueOf(TEST_PROTEIN_VERSION)))
                .thenReturn(Optional.of(proteinEntity));

        FileInputStream inputStream = getInputStreamFromFile(TEST_PROTEIN_FILE_PATH);

        assertThrows(
                FileAlreadyExistsException.class,
                ()-> new ProteinFileUploadService(mockGenBankRepository).uploadFile(inputStream)
        );
    }

    private FileInputStream getInputStreamFromFile(String filename) throws FileNotFoundException {
        return new FileInputStream(filename);
    }

    private InputStream getInputStreamFromContent(String content) {
        return new ByteArrayInputStream(content.getBytes());
    }
}
