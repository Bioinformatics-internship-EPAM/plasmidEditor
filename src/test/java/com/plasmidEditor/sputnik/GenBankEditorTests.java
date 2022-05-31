package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.editor.EditorParameters;
import com.plasmidEditor.sputnik.editor.GenBankEditor;
import com.plasmidEditor.sputnik.services.GenBankService;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.AbstractFeature;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GenBankEditorTests {
    private static final String DNA_SEQUENCE = "AAATTTGGG";
    private static final int DNA_SEQUENCE_LENGTH = 9;
    private static final int POSITION = 55;
    private static final int CUT_SIZE = 9;

    private static final String DNA_FILE_PATH = "src\\test\\resources\\X81322.gb";
    private static final String DNA_ACCESSION = "X81322";
    private static final String DNA_VERSION = "1";
    private static String DNA_FILE_CONTENT;
    private static String INIT_DNA_SEQUENCE;

    private static GenbankSequenceParser<AbstractSequence<Compound>, Compound> parser;
    private static GenBankEditor editor;

    @BeforeAll
    public static void init() throws IOException {
        DNA_FILE_CONTENT = Files.readString(Path.of(DNA_FILE_PATH));

        BufferedReader bufferedReader = new BufferedReader(new StringReader(DNA_FILE_CONTENT));
        parser = new GenbankSequenceParser<>();
        INIT_DNA_SEQUENCE = parser.getSequence(bufferedReader, 0);
        bufferedReader.close();

        GenBankDTO testDTO = GenBankDTO.builder()
                .accession(DNA_ACCESSION)
                .version(DNA_VERSION)
                .file(DNA_FILE_CONTENT)
                .build();

        GenBankService serviceMock = Mockito.mock(GenBankService.class);
        Mockito.when(serviceMock.get(DNA_ACCESSION, DNA_VERSION)).thenReturn(testDTO);

        editor = new GenBankEditor(serviceMock);
    }

    @Test
    public void addDNASequenceTest() {
        EditorParameters parameters = new EditorParameters(DNA_SEQUENCE, POSITION, DNA_ACCESSION, DNA_VERSION, CUT_SIZE);
        DNASequence newSequence = editor.add(parameters);
        checkCorrectDNASequenceInsert(newSequence);
        checkDNASequenceLocationsAfterInsert(newSequence);
    }

    @Test
    public void modifyDNASequenceTest() {
        EditorParameters parameters = new EditorParameters(DNA_SEQUENCE, POSITION, DNA_ACCESSION, DNA_VERSION, CUT_SIZE);
        DNASequence newSequence = editor.modify(parameters);
        checkCorrectDNASequenceModify(newSequence);
        checkDNASequenceLocationsAfterModify(newSequence);
    }

    @Test
    public void cutDNASequenceTest() {
        EditorParameters parameters = new EditorParameters(DNA_SEQUENCE, POSITION, DNA_ACCESSION, DNA_VERSION, CUT_SIZE);
        DNASequence newSequence = editor.cut(parameters);
        checkCorrectDNASequenceCut(newSequence);
        checkDNASequenceLocationsAfterCut(newSequence);
    }

    private void checkCorrectDNASequenceInsert(DNASequence newSequence) {
        String newSequenceString = newSequence.getSequenceAsString();
        int newSequenceLength = newSequence.getLength();

        assertEquals(INIT_DNA_SEQUENCE.length(), newSequence.getLength() - DNA_SEQUENCE_LENGTH);
        assertEquals(DNA_SEQUENCE, newSequenceString.substring(POSITION - 1, POSITION - 1 + DNA_SEQUENCE_LENGTH));
        assertEquals(
                INIT_DNA_SEQUENCE,
                newSequenceString.substring(0, POSITION - 1)
                        + newSequenceString.substring(POSITION - 1 + DNA_SEQUENCE_LENGTH, newSequenceLength)
        );
    }

    private void checkCorrectDNASequenceModify(DNASequence newSequence) {
        String newSequenceString = newSequence.getSequenceAsString();
        int newSequenceLength = newSequence.getLength();

        assertEquals(INIT_DNA_SEQUENCE.length(), newSequenceLength);
        assertEquals(DNA_SEQUENCE, newSequenceString.substring(POSITION - 1, POSITION - 1 + DNA_SEQUENCE_LENGTH));
        assertEquals(
                INIT_DNA_SEQUENCE.substring(0, POSITION - 1)
                        + INIT_DNA_SEQUENCE.substring(POSITION - 1 + DNA_SEQUENCE_LENGTH),
                newSequenceString.substring(0, POSITION - 1)
                        + newSequenceString.substring(POSITION - 1 + DNA_SEQUENCE_LENGTH, newSequenceLength)
        );
    }

    private void checkCorrectDNASequenceCut(DNASequence newSequence) {
        String newSequenceString = newSequence.getSequenceAsString();
        int newSequenceLength = newSequence.getLength();

        assertEquals(INIT_DNA_SEQUENCE.length() - DNA_SEQUENCE_LENGTH, newSequenceLength);
        assertEquals(
                INIT_DNA_SEQUENCE.substring(0, POSITION - 1)
                        + INIT_DNA_SEQUENCE.substring(POSITION - 1 + DNA_SEQUENCE_LENGTH),
                newSequenceString);
    }

    private void checkDNASequenceLocationsAfterInsert(DNASequence newSequence) {
        Map<String, List<AbstractFeature<AbstractSequence<Compound>, Compound>>> initFeatures = parser.getFeatures();
        List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> newFeatures = newSequence.getFeatures();

        assertEquals(initFeatures.size(), newFeatures.size());

        for (var newF: newFeatures) {
            FeatureInterface<AbstractSequence<Compound>, Compound> initF = initFeatures.get(newF.getDescription()).get(0);
            int initFStart = initF.getLocations().getStart().getPosition();
            int initFEnd = initF.getLocations().getEnd().getPosition();
            int newFStart = newF.getLocations().getStart().getPosition();
            int newFEnd = newF.getLocations().getEnd().getPosition();

            if (newF.getDescription().equals("source"))
                assertEquals(initFStart, newFStart);
            else
                assertEquals(initFStart + DNA_SEQUENCE_LENGTH, newFStart);

            assertEquals(initFEnd + DNA_SEQUENCE_LENGTH, newFEnd);
        }
    }

    private void checkDNASequenceLocationsAfterModify(DNASequence newSequence) {
        Map<String, List<AbstractFeature<AbstractSequence<Compound>, Compound>>> initFeatures = parser.getFeatures();
        List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> newFeatures = newSequence.getFeatures();

        assertEquals(initFeatures.size(), newFeatures.size());

        for (var newF: newFeatures) {
            FeatureInterface<AbstractSequence<Compound>, Compound> initF = initFeatures.get(newF.getDescription()).get(0);
            int initFStart = initF.getLocations().getStart().getPosition();
            int initFEnd = initF.getLocations().getEnd().getPosition();
            int newFStart = newF.getLocations().getStart().getPosition();
            int newFEnd = newF.getLocations().getEnd().getPosition();

            assertEquals(initFStart, newFStart);
            assertEquals(initFEnd, newFEnd);
        }
    }

    private void checkDNASequenceLocationsAfterCut(DNASequence newSequence) {
        Map<String, List<AbstractFeature<AbstractSequence<Compound>, Compound>>> initFeatures = parser.getFeatures();
        List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> newFeatures = newSequence.getFeatures();

        assertEquals(initFeatures.size(), newFeatures.size());

        for (var newF: newFeatures) {
            FeatureInterface<AbstractSequence<Compound>, Compound> initF = initFeatures.get(newF.getDescription()).get(0);
            int initFStart = initF.getLocations().getStart().getPosition();
            int initFEnd = initF.getLocations().getEnd().getPosition();
            int newFStart = newF.getLocations().getStart().getPosition();
            int newFEnd = newF.getLocations().getEnd().getPosition();

            if (newF.getDescription().equals("source"))
                assertEquals(initFStart, newFStart);
            else
                assertEquals(POSITION, newFStart);

            assertEquals(initFEnd - DNA_SEQUENCE_LENGTH, newFEnd);
        }
    }
}
