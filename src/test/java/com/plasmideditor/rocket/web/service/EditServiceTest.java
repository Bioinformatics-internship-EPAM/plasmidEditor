package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.genbank.io.dna.GenBankDNAFileReader;
import com.plasmideditor.rocket.genbank.io.protein.GenBankProteinFileReader;
import com.plasmideditor.rocket.genbank.repository.GenBankRepository;
import com.plasmideditor.rocket.web.domains.GenBankEntity;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileEditorException;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import com.plasmideditor.rocket.web.service.exceptions.UnknownSequenceType;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
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
    private final String TEST_DNA_SEQUENCE = "AAAAAAAAAA";
    private final String TEST_PROTEIN_SEQUENCE = "MASMASMASM";
    private final Integer TEST_START_POSITION = 3;
    private final Integer TEST_SEQUENCE_LENGTH = 10;

    private static final String TEST_DNA_FILE_PATH = "src/test/resources/BI431008.gb";
    private static final String TEST_PROTEIN_FILE_PATH = "src/test/resources/3MJ8_A.gb";


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
        assertDoesNotThrow(() -> {
            DNASequence initialSequence = new GenBankDNAFileReader().read_sequence(TEST_DNA_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("1", "v1");
            DNASequence modifiedFileContent = editService.addGenBankFile(TEST_START_POSITION, TEST_DNA_SEQUENCE, testFileContent, DNASequence.class);
            String modifiedSequence = modifiedFileContent.getSequenceAsString();
            assertEquals(TEST_SEQUENCE_LENGTH + initialSequence.getLength(), modifiedFileContent.getLength());
            assertEquals(TEST_DNA_SEQUENCE, modifiedSequence.substring(TEST_START_POSITION, TEST_SEQUENCE_LENGTH + TEST_START_POSITION));
        });
    }

    @Test
    public void testAddProteinSequenceToGenBankFile() {
        assertDoesNotThrow(() -> {
            ProteinSequence initialSequence = new GenBankProteinFileReader().read_sequence(TEST_PROTEIN_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("2", "v1");
            ProteinSequence modifiedFileContent = editService.addGenBankFile(TEST_START_POSITION, TEST_PROTEIN_SEQUENCE, testFileContent, ProteinSequence.class);
            String modifiedSequence = modifiedFileContent.getSequenceAsString();
            assertEquals(TEST_SEQUENCE_LENGTH + initialSequence.getLength(), modifiedFileContent.getLength());
            assertEquals(TEST_PROTEIN_SEQUENCE, modifiedSequence.substring(TEST_START_POSITION, TEST_SEQUENCE_LENGTH + TEST_START_POSITION));
        });
    }

    @Test
    public void testFeaturesModifiedCorrectlyDuringAddSequenceWhenFeatureStartBeforeSequenceStart() {
        assertDoesNotThrow(() -> {
            DNASequence initialSequence = new GenBankDNAFileReader().read_sequence(TEST_DNA_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("1", "v1");
            DNASequence modifiedFileContent = editService.addGenBankFile(TEST_START_POSITION, TEST_DNA_SEQUENCE, testFileContent, DNASequence.class);

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

    @Test
    public void testCutDNASequenceToGenBankFile() {
        int sequenceEndPosition = TEST_START_POSITION + TEST_SEQUENCE_LENGTH;
        assertDoesNotThrow(() -> {
            DNASequence initialSequence = new GenBankDNAFileReader().read_sequence(TEST_DNA_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("1", "v1");
            String sequenceToCut = initialSequence.getSequenceAsString().substring(TEST_START_POSITION, sequenceEndPosition);
            DNASequence modifiedFileContent = editService.cutGenBankFile(TEST_START_POSITION, sequenceToCut, testFileContent, DNASequence.class);
            String modifiedSequence = modifiedFileContent.getSequenceAsString();
            assertEquals(initialSequence.getLength() - TEST_SEQUENCE_LENGTH, modifiedFileContent.getLength());
            assertEquals(initialSequence.getSequenceAsString().substring(0, TEST_START_POSITION) + initialSequence.getSequenceAsString().substring(sequenceEndPosition),
                    modifiedSequence);
        });
    }

    @Test
    public void testCutProteinSequenceToGenBankFile() {
        int sequenceEndPosition = TEST_START_POSITION + TEST_SEQUENCE_LENGTH;
        assertDoesNotThrow(() -> {
            ProteinSequence initialSequence = new GenBankProteinFileReader().read_sequence(TEST_PROTEIN_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("2", "v1");
            String sequenceToCut = initialSequence.getSequenceAsString().substring(TEST_START_POSITION, sequenceEndPosition);
            ProteinSequence modifiedFileContent = editService.cutGenBankFile(TEST_START_POSITION, sequenceToCut, testFileContent, ProteinSequence.class);
            String modifiedSequence = modifiedFileContent.getSequenceAsString();
            assertEquals(initialSequence.getLength() - TEST_SEQUENCE_LENGTH, modifiedFileContent.getLength());
            assertEquals(initialSequence.getSequenceAsString().substring(0, TEST_START_POSITION) + initialSequence.getSequenceAsString().substring(sequenceEndPosition),
                    modifiedSequence);
        });
    }

    @Test
    public void testCutSequenceWithIncorrectSequenceToCut() {
        assertDoesNotThrow(() -> {
            String testFileContent = editService.getFileFromDB("2", "v1");
            assertThrows(GenBankFileEditorException.class,
                    () ->  editService.cutGenBankFile(TEST_START_POSITION, TEST_PROTEIN_SEQUENCE, testFileContent, ProteinSequence.class)
            );
        });
    }

    @Test
    public void testFeaturesModifiedCorrectlyDuringCutSequenceWhenFeatureStartAndEndBeforeSequenceStart() {
        int sequenceStartPosition = 120;
        assertDoesNotThrow(() -> {
            ProteinSequence initialSequence = new GenBankProteinFileReader().read_sequence(TEST_PROTEIN_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("2", "v1");
            String sequenceToCut = initialSequence.getSequenceAsString().substring(sequenceStartPosition, sequenceStartPosition + TEST_SEQUENCE_LENGTH);
            ProteinSequence modifiedFileContent = editService.cutGenBankFile(sequenceStartPosition, sequenceToCut, testFileContent, ProteinSequence.class);

            List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> initialFeatures = initialSequence.getFeatures();
            List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> modifiedFeatures = modifiedFileContent.getFeatures();
            assertEquals(initialFeatures.size(), modifiedFeatures.size());
            assertEquals(initialFeatures.get(1).getDescription(), modifiedFeatures.get(1).getDescription());
            assertEquals(initialFeatures.get(1).getLocations().getStart(), modifiedFeatures.get(1).getLocations().getStart());
            assertEquals(initialFeatures.get(1).getLocations().getEnd(), modifiedFeatures.get(1).getLocations().getEnd());
        });
    }

    @Test
    public void testFeaturesModifiedCorrectlyDuringCutSequenceWhenFeatureStartAndEndAfterSequenceEnd() {
        assertDoesNotThrow(() -> {
            ProteinSequence initialSequence = new GenBankProteinFileReader().read_sequence(TEST_PROTEIN_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("2", "v1");
            String sequenceToCut = initialSequence.getSequenceAsString().substring(TEST_START_POSITION, TEST_START_POSITION + TEST_SEQUENCE_LENGTH);
            ProteinSequence modifiedFileContent = editService.cutGenBankFile(TEST_START_POSITION, sequenceToCut, testFileContent, ProteinSequence.class);

            List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> initialFeatures = initialSequence.getFeatures();
            List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> modifiedFeatures = modifiedFileContent.getFeatures();
            assertEquals(initialFeatures.size(), modifiedFeatures.size());
            assertEquals(initialFeatures.get(4).getDescription(), modifiedFeatures.get(4).getDescription());
            assertEquals(initialFeatures.get(4).getLocations().getStart().getPosition() - TEST_SEQUENCE_LENGTH,
                    modifiedFeatures.get(4).getLocations().getStart().getPosition());
            assertEquals(initialFeatures.get(4).getLocations().getEnd().getPosition() - TEST_SEQUENCE_LENGTH,
                    modifiedFeatures.get(4).getLocations().getEnd().getPosition());
        });
    }

    @Test
    public void testFeaturesModifiedCorrectlyDuringCutSequenceWhenFeatureStartBeforeSequenceStartAndEndAfterSequenceEnd() {
        assertDoesNotThrow(() -> {
            ProteinSequence initialSequence = new GenBankProteinFileReader().read_sequence(TEST_PROTEIN_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("2", "v1");
            String sequenceToCut = initialSequence.getSequenceAsString().substring(TEST_START_POSITION, TEST_START_POSITION + TEST_SEQUENCE_LENGTH);
            ProteinSequence modifiedFileContent = editService.cutGenBankFile(TEST_START_POSITION, sequenceToCut, testFileContent, ProteinSequence.class);

            List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> initialFeatures = initialSequence.getFeatures();
            List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> modifiedFeatures = modifiedFileContent.getFeatures();
            assertEquals(initialFeatures.size(), modifiedFeatures.size());
            assertEquals(initialFeatures.get(0).getDescription(), modifiedFeatures.get(0).getDescription());
            assertEquals(initialFeatures.get(0).getLocations().getStart(), modifiedFeatures.get(0).getLocations().getStart());
            assertEquals(initialFeatures.get(0).getLocations().getEnd().getPosition() - TEST_SEQUENCE_LENGTH,
                    modifiedFeatures.get(0).getLocations().getEnd().getPosition());
        });
    }

    @Test
    public void testFeaturesModifiedCorrectlyDuringCutSequenceWhenFeatureStartBeforeSequenceStartAndEndBetweenSequenceStartAndEnd() {
        int sequenceStartPosition = 110;
        assertDoesNotThrow(() -> {
            ProteinSequence initialSequence = new GenBankProteinFileReader().read_sequence(TEST_PROTEIN_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("2", "v1");
            String sequenceToCut = initialSequence.getSequenceAsString().substring(sequenceStartPosition, sequenceStartPosition + TEST_SEQUENCE_LENGTH);
            ProteinSequence modifiedFileContent = editService.cutGenBankFile(sequenceStartPosition, sequenceToCut, testFileContent, ProteinSequence.class);

            List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> initialFeatures = initialSequence.getFeatures();
            List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> modifiedFeatures = modifiedFileContent.getFeatures();
            assertEquals(initialFeatures.size(), modifiedFeatures.size());
            assertEquals(initialFeatures.get(1).getDescription(), modifiedFeatures.get(1).getDescription());
            assertEquals(initialFeatures.get(1).getLocations().getStart(), modifiedFeatures.get(1).getLocations().getStart());
            assertEquals(sequenceStartPosition, modifiedFeatures.get(1).getLocations().getEnd().getPosition());
        });
    }

    @Test
    public void testFeaturesModifiedCorrectlyDuringCutSequenceWhenFeatureStartBetweenSequenceStartAndEndAndEndAfterSequenceEnd() {
        assertDoesNotThrow(() -> {
            ProteinSequence initialSequence = new GenBankProteinFileReader().read_sequence(TEST_PROTEIN_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("2", "v1");
            String sequenceToCut = initialSequence.getSequenceAsString().substring(TEST_START_POSITION, TEST_START_POSITION + TEST_SEQUENCE_LENGTH);
            ProteinSequence modifiedFileContent = editService.cutGenBankFile(TEST_START_POSITION, sequenceToCut, testFileContent, ProteinSequence.class);

            List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> initialFeatures = initialSequence.getFeatures();
            List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> modifiedFeatures = modifiedFileContent.getFeatures();
            assertEquals(initialFeatures.size(), modifiedFeatures.size());
            assertEquals(initialFeatures.get(3).getDescription(), modifiedFeatures.get(3).getDescription());
            assertEquals(TEST_START_POSITION, modifiedFeatures.get(3).getLocations().getStart().getPosition());
            assertEquals(initialFeatures.get(3).getLocations().getEnd().getPosition() - TEST_SEQUENCE_LENGTH,
                    modifiedFeatures.get(3).getLocations().getEnd().getPosition());
        });
    }

    @Test
    public void testFeaturesModifiedCorrectlyDuringCutSequenceWhenFeatureStartAndEndBetweenSequenceStartAndEnd() {
        int sequenceStartPosition = 6;
        assertDoesNotThrow(() -> {
            ProteinSequence initialSequence = new GenBankProteinFileReader().read_sequence(TEST_PROTEIN_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("2", "v1");
            String sequenceToCut = initialSequence.getSequenceAsString().substring(sequenceStartPosition, sequenceStartPosition + TEST_SEQUENCE_LENGTH);
            ProteinSequence modifiedFileContent = editService.cutGenBankFile(sequenceStartPosition, sequenceToCut, testFileContent, ProteinSequence.class);

            List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> initialFeatures = initialSequence.getFeatures();
            List<FeatureInterface<AbstractSequence<AminoAcidCompound>, AminoAcidCompound>> modifiedFeatures = modifiedFileContent.getFeatures();
            assertEquals(initialFeatures.size() - 1, modifiedFeatures.size());
        });
    }

    @Test
    public void testModifyDNASequenceToGenBankFile() {
        assertDoesNotThrow(() -> {
            DNASequence initialSequence = new GenBankDNAFileReader().read_sequence(TEST_DNA_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("1", "v1");
            DNASequence modifiedFileContent = editService.modifyGenBankFile(TEST_START_POSITION, TEST_DNA_SEQUENCE, testFileContent, DNASequence.class);
            String modifiedSequence = modifiedFileContent.getSequenceAsString();
            assertEquals(initialSequence.getLength(), modifiedFileContent.getLength());
            assertEquals(initialSequence.getSequenceAsString().substring(0, TEST_START_POSITION)
                            + TEST_DNA_SEQUENCE
                            + initialSequence.getSequenceAsString().substring(TEST_START_POSITION + TEST_SEQUENCE_LENGTH),
                    modifiedSequence);
        });
    }

    @Test
    public void testModifyProteinSequenceToGenBankFile() {
        assertDoesNotThrow(() -> {
            ProteinSequence initialSequence = new GenBankProteinFileReader().read_sequence(TEST_PROTEIN_FILE_PATH).get(0);
            String testFileContent = editService.getFileFromDB("2", "v1");
            ProteinSequence modifiedFileContent = editService.modifyGenBankFile(TEST_START_POSITION, TEST_PROTEIN_SEQUENCE, testFileContent, ProteinSequence.class);
            String modifiedSequence = modifiedFileContent.getSequenceAsString();
            assertEquals(initialSequence.getLength(), modifiedFileContent.getLength());
            assertEquals(initialSequence.getSequenceAsString().substring(0, TEST_START_POSITION)
                            + TEST_PROTEIN_SEQUENCE
                            + initialSequence.getSequenceAsString().substring(TEST_START_POSITION + TEST_SEQUENCE_LENGTH),
                    modifiedSequence);
        });
    }

    @Test
    public void testModifySequenceWithIncorrectSequenceToModify() {
        int sequenceStartPosition = 210;
        assertDoesNotThrow(() -> {
            String testFileContent = editService.getFileFromDB("2", "v1");
            assertThrows(GenBankFileEditorException.class,
                    () ->  editService.modifyGenBankFile(sequenceStartPosition, TEST_PROTEIN_SEQUENCE, testFileContent, ProteinSequence.class)
            );
        });
    }
}
