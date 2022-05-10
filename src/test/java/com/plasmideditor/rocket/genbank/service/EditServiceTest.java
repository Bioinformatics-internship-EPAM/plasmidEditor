package com.plasmideditor.rocket.genbank.service;

import com.plasmideditor.rocket.web.domains.GenBankEntity;
import com.plasmideditor.rocket.genbank.io.dna.GenBankDNAFileReader;
import com.plasmideditor.rocket.genbank.io.protein.GenBankProteinFileReader;
import com.plasmideditor.rocket.genbank.repository.GenBankRepository;
import com.plasmideditor.rocket.web.service.EditService;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import com.plasmideditor.rocket.web.service.exceptions.UnknownSequenceType;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EditServiceTest {
    private String TEST_DNA_SEQUENCE = "AAAAAAAAAA";
    private String TEST_PROTEIN_SEQUENCE = "MASMASMASM";
    private Integer TEST_SEQUENCE_LENGTH = 10;

    private static String TEST_DNA_FILE_PATH = "src/test/resources/BI431008.gb";
    private static String TEST_PROTEIN_FILE_PATH = "src/test/resources/3MJ8_A.gb";


    private static EditService editService;

    @BeforeAll
    public static void init() throws IOException {
        String testDNAFileContent = Files.readString(Path.of(TEST_DNA_FILE_PATH));
        GenBankEntity dnaEntity = new GenBankEntity("1", "v1", testDNAFileContent);

        String testProteinFileContent = Files.readString(Path.of(TEST_PROTEIN_FILE_PATH));
        GenBankEntity proteinEntity = new GenBankEntity("2", "v1", testProteinFileContent);

        GenBankRepository mockGenBankRepository = Mockito.mock(GenBankRepository.class);
        Mockito.when(mockGenBankRepository.findByAccessionIdAndVersion("1", "v1")).thenReturn(Optional.of(dnaEntity));
        Mockito.when(mockGenBankRepository.findByAccessionIdAndVersion("2", "v1")).thenReturn(Optional.of(proteinEntity));
        Mockito.when(mockGenBankRepository.findByAccessionIdAndVersion("11", "v1")).thenReturn(Optional.empty());
        editService = new EditService(mockGenBankRepository);
    }

    @Test
    public void testGetFileFromDbSuccessfully() {
        assertDoesNotThrow(() -> {
            String file = editService.getFileFromDB("1", "v1");
            assertNotNull(file);
        });
    }

    @Test
    public void testGetFileFromDbWhenFileDoesNotExist() {
        assertThrows(FileNotFoundException.class,
                () -> editService.getFileFromDB("11", "v1"),
                "Expected getFileFromDB() to throw FileNotFoundException, but it didn't"
        );
    }

    @Test
    public void testValidateCorrectDNASequence() {
        String correctSequence = "ACTGACTG";
        assertDoesNotThrow(() -> {
            editService.validateSequence(correctSequence, DNASequence.class);
        });
    }

    @Test
    public void testValidateIncorrectDNASequence() {
        String incorrectSequence = "NOTDNASEQ";
        assertThrows(SequenceValidationException.class,
                () -> {
                    editService.validateSequence(incorrectSequence, DNASequence.class);
                });
    }

    @Test
    public void testValidateCorrectProteinSequence() {
        String correctSequence = "MSAMSAMSA";
        assertDoesNotThrow(() -> {
            editService.validateSequence(correctSequence, ProteinSequence.class);
        });
    }

    @Test
    public void testValidateIncorrectProteinSequence() {
        String incorrectSequence = "NOTPROTEINSEQ";
        assertThrows(SequenceValidationException.class,
                () -> {
                    editService.validateSequence(incorrectSequence, ProteinSequence.class);
                });
    }

    @Test
    public void testGetDnaSequenceType() {
        assertDoesNotThrow(() -> {
            String testDNAFileContent = Files.readString(Path.of(TEST_DNA_FILE_PATH));
            String sequenceTypeName = editService.getSequenceType(testDNAFileContent).getSimpleName();
            assertEquals("DNASequence", sequenceTypeName);
        });
    }

    @Test
    public void testGetProteinSequenceType() {
        assertDoesNotThrow(() -> {
            String testProteinFileContent = Files.readString(Path.of(TEST_PROTEIN_FILE_PATH));
            String sequenceTypeName = editService.getSequenceType(testProteinFileContent).getSimpleName();
            assertEquals("ProteinSequence", sequenceTypeName);
        });
    }

    @Test
    public void testGetUnknownSequenceType() {
        assertThrows(UnknownSequenceType.class, () -> {
            editService.getSequenceType("some wrong files data\n");
        });
    }


    @Test
    public void testAddDNASequenceToGenBankFile() {
        int sequenceStartPosition = 3;
        assertDoesNotThrow(() -> {
            DNASequence initialSequence = new GenBankDNAFileReader().read_sequence(TEST_DNA_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("1", "v1");
            DNASequence modifiedFileContent = editService.addGenBankFile(sequenceStartPosition, TEST_DNA_SEQUENCE, testFileContent, DNASequence.class);
            String modifiedSequence = modifiedFileContent.getSequenceAsString();
            assertEquals(TEST_SEQUENCE_LENGTH + initialSequence.getLength(), modifiedFileContent.getLength());
            assertEquals(TEST_DNA_SEQUENCE, modifiedSequence.substring(sequenceStartPosition, TEST_SEQUENCE_LENGTH + sequenceStartPosition));
        });
    }

    @Test
    public void testAddProteinSequenceToGenBankFile() {
        int sequenceStartPosition = 3;
        assertDoesNotThrow(() -> {
            ProteinSequence initialSequence = new GenBankProteinFileReader().read_sequence(TEST_PROTEIN_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("2", "v1");
            ProteinSequence modifiedFileContent = editService.addGenBankFile(sequenceStartPosition, TEST_PROTEIN_SEQUENCE, testFileContent, ProteinSequence.class);
            String modifiedSequence = modifiedFileContent.getSequenceAsString();
            assertEquals(TEST_SEQUENCE_LENGTH + initialSequence.getLength(), modifiedFileContent.getLength());
            assertEquals(TEST_PROTEIN_SEQUENCE, modifiedSequence.substring(sequenceStartPosition, TEST_SEQUENCE_LENGTH + sequenceStartPosition));
        });
    }

    @Test
    public void testFeaturesModifiedCorrectlyDuringAddSequenceWhenFeatureStartBeforeSequenceStart() {
        int sequenceStartPosition = 3;
        assertDoesNotThrow(() -> {
            DNASequence initialSequence = new GenBankDNAFileReader().read_sequence(TEST_DNA_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("1", "v1");
            DNASequence modifiedFileContent = editService.addGenBankFile(sequenceStartPosition, TEST_DNA_SEQUENCE, testFileContent, DNASequence.class);

            List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> initialFeatures = initialSequence.getFeatures();
            List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> modifiedFeatures = modifiedFileContent.getFeatures();
            assertEquals(initialFeatures.size(), modifiedFeatures.size());
            assertEquals(initialFeatures.get(0).getDescription(), modifiedFeatures.get(0).getDescription());
            assertEquals(initialFeatures.get(0).getLocations().getStart(), modifiedFeatures.get(0).getLocations().getStart());
            assertEquals(initialFeatures.get(0).getLocations().getEnd().getPosition() + TEST_SEQUENCE_LENGTH,
                    modifiedFeatures.get(0).getLocations().getEnd().getPosition());
        });
    }

    @Test
    public void testFeaturesModifiedCorrectlyDuringAddSequenceWhenFeatureStartEqualsSequenceStart() {
        int sequenceStartPosition = 1;
        assertDoesNotThrow(() -> {
            DNASequence initialSequence = new GenBankDNAFileReader().read_sequence(TEST_DNA_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("1", "v1");
            DNASequence modifiedFileContent = editService.addGenBankFile(sequenceStartPosition, TEST_DNA_SEQUENCE, testFileContent, DNASequence.class);

            List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> initialFeatures = initialSequence.getFeatures();
            List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> modifiedFeatures = modifiedFileContent.getFeatures();
            assertEquals(initialFeatures.size(), modifiedFeatures.size());
            assertEquals(initialFeatures.get(0).getDescription(), modifiedFeatures.get(0).getDescription());
            assertEquals(initialFeatures.get(0).getLocations().getStart(), modifiedFeatures.get(0).getLocations().getStart());
            assertEquals(initialFeatures.get(0).getLocations().getEnd().getPosition() + TEST_SEQUENCE_LENGTH,
                    modifiedFeatures.get(0).getLocations().getEnd().getPosition());
        });
    }
}