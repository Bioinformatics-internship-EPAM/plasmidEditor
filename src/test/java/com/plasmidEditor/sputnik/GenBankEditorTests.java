package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.editor.GenBankEditor;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.AbstractFeature;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class GenBankEditorTests {
    private final String DNA_FILE_PATH = "src\\test\\resources\\X81322.gb";
    private final String DNA_SEQUENCE = "AAATTTGGG";
    private final int POSITION = 55;
    private final int CUT_SIZE = 9;
    private final GenBankEditor editor = new GenBankEditor();

    @Test
    void addDNASequenceTest()  throws IOException {
        String DNAFile = Files.readString(Path.of(DNA_FILE_PATH));
        BufferedReader bufferedReader = new BufferedReader(new StringReader(DNAFile));
        GenbankSequenceParser<AbstractSequence<Compound>, Compound> parser = new GenbankSequenceParser<>();
        String initSequence = parser.getSequence(bufferedReader, 0);
        DNASequence newSequence = editor.add(DNA_SEQUENCE, POSITION, DNAFile);

        assertEquals(initSequence.length(), newSequence.getLength() - DNA_SEQUENCE.length());
        assertEquals(
                DNA_SEQUENCE,
                newSequence.getSequenceAsString().substring(POSITION - 1, POSITION - 1 + DNA_SEQUENCE.length())
        );
        assertEquals(
                initSequence,
                newSequence.getSequenceAsString().substring(0, POSITION - 1) + newSequence.getSequenceAsString()
                        .substring(POSITION - 1 + DNA_SEQUENCE.length(), newSequence.getLength())
        );

        Map<String, List<AbstractFeature<AbstractSequence<Compound>, Compound>>> initFeatures = parser.getFeatures();
        List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> newFeatures = newSequence.getFeatures();

        assertEquals(initFeatures.size(), newFeatures.size());

        for (var f: newFeatures) {
            FeatureInterface<AbstractSequence<Compound>, Compound> temp = initFeatures.get(f.getDescription()).get(0);
            if (f.getDescription().equals("source")) {
                assertEquals(
                        temp.getLocations().getStart().getPosition(),
                        f.getLocations().getStart().getPosition()
                );
                assertEquals(
                        temp.getLocations().getEnd().getPosition() + DNA_SEQUENCE.length(),
                        f.getLocations().getEnd().getPosition()
                );
            }
            else {
                assertEquals(
                        temp.getLocations().getStart().getPosition() + DNA_SEQUENCE.length(),
                        f.getLocations().getStart().getPosition()
                );
                assertEquals(
                        temp.getLocations().getEnd().getPosition() + DNA_SEQUENCE.length(),
                        f.getLocations().getEnd().getPosition()
                );
            }
        }
    }

    @Test
    void modifyDNASequenceTest() throws IOException {
        String DNAFile = Files.readString(Path.of(DNA_FILE_PATH));
        BufferedReader bufferedReader = new BufferedReader(new StringReader(DNAFile));
        GenbankSequenceParser<AbstractSequence<Compound>, Compound> parser = new GenbankSequenceParser<>();
        String initSequence = parser.getSequence(bufferedReader, 0);
        DNASequence sequence = editor.modify(DNA_SEQUENCE, POSITION, DNAFile);

        assertEquals(initSequence.length(), sequence.getLength());
        assertEquals(
                DNA_SEQUENCE,
                sequence.getSequenceAsString().substring(POSITION - 1, POSITION - 1 + DNA_SEQUENCE.length())
        );
        assertEquals(
                initSequence.substring(0, POSITION - 1) + initSequence.substring(POSITION - 1 + DNA_SEQUENCE.length()),
                sequence.getSequenceAsString().substring(0, POSITION - 1) + sequence.getSequenceAsString()
                        .substring(POSITION - 1 + DNA_SEQUENCE.length(), sequence.getLength())
        );

        Map<String, List<AbstractFeature<AbstractSequence<Compound>, Compound>>> initFeatures = parser.getFeatures();
        List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> newFeatures = sequence.getFeatures();

        assertEquals(initFeatures.size(), newFeatures.size());

        for (var f: newFeatures) {
            FeatureInterface<AbstractSequence<Compound>, Compound> temp = initFeatures.get(f.getDescription()).get(0);
            assertEquals(temp.getLocations().getStart().getPosition(), f.getLocations().getStart().getPosition());
            assertEquals(temp.getLocations().getEnd().getPosition(), f.getLocations().getEnd().getPosition());
        }
    }

    @Test
    void cutDNASequenceTest() throws IOException {
        GenBankEditor editor = new GenBankEditor();
        String DNAFile = Files.readString(Path.of(DNA_FILE_PATH));
        BufferedReader bufferedReader = new BufferedReader(new StringReader(DNAFile));
        GenbankSequenceParser<AbstractSequence<Compound>, Compound> parser = new GenbankSequenceParser<>();
        String initSequence = parser.getSequence(bufferedReader, 0);
        DNASequence sequenceAdd = editor.cut(POSITION, CUT_SIZE, DNAFile);

        assertEquals(initSequence.length() - DNA_SEQUENCE.length(), sequenceAdd.getLength());
        assertEquals(
                initSequence.substring(0, POSITION - 1) + initSequence.substring(POSITION - 1 + DNA_SEQUENCE.length()),
                sequenceAdd.getSequenceAsString()
        );

        Map<String, List<AbstractFeature<AbstractSequence<Compound>, Compound>>> initFeatures = parser.getFeatures();
        List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> newFeatures = sequenceAdd.getFeatures();

        assertEquals(initFeatures.size(), newFeatures.size());
        for (var f: newFeatures) {
            FeatureInterface<AbstractSequence<Compound>, Compound> temp = initFeatures.get(f.getDescription()).get(0);
            if (Objects.equals(f.getDescription(), "source")) {
                assertEquals(temp.getLocations().getStart().getPosition(), f.getLocations().getStart().getPosition());
                assertEquals(
                        temp.getLocations().getEnd().getPosition() - DNA_SEQUENCE.length(),
                        f.getLocations().getEnd().getPosition()
                );
            }
            else {
                assertEquals(POSITION, f.getLocations().getStart().getPosition());
                assertEquals(
                        temp.getLocations().getEnd().getPosition() - DNA_SEQUENCE.length(),
                        f.getLocations().getEnd().getPosition()
                );
            }
        }
    }
}
