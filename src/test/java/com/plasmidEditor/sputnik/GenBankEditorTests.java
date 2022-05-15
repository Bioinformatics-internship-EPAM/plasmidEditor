package com.plasmidEditor.sputnik;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.features.AbstractFeature;
import org.junit.jupiter.api.Test;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.io.GenbankSequenceParser;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GenBankEditorTests {
    private String TestDNASequence = "AAATTTGGG";
    private int position = 55;
    private int length = TestDNASequence.length();

    @Test
    void addDNASequenceTest()  throws IOException {
        GenBankEditor editor = new GenBankEditor();
        String DNAFile = Files.readString(Path.of("src\\test\\resources\\X81322.gb"));
        BufferedReader bufferedReader = new BufferedReader(new StringReader(DNAFile));
        GenbankSequenceParser parser = new GenbankSequenceParser<>();

        DNASequence sequenceAdd = editor.add(TestDNASequence, position, DNAFile, DNASequence.class);

        String initSequence = parser.getSequence(bufferedReader, 0);
        assertEquals(initSequence.length(), sequenceAdd.getLength() - length);
        assertEquals(TestDNASequence, sequenceAdd.getSequenceAsString().substring(position - 1, position - 1 + length));
        assertEquals(initSequence, sequenceAdd.getSequenceAsString().substring(0, position - 1) + sequenceAdd.getSequenceAsString().substring(position - 1 + length, sequenceAdd.getLength()));

        Map<String, List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>>> initFeatures = parser.getFeatures();
        List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> newFeatures = sequenceAdd.getFeatures();

        assertEquals(initFeatures.size(), newFeatures.size());
        for (var f: newFeatures) {
            var temp= initFeatures.get(f.getDescription()).get(0);
            if (Objects.equals(f.getDescription(), "source")) {
                assertEquals(temp.getLocations().getStart().getPosition(), f.getLocations().getStart().getPosition());
                assertEquals(temp.getLocations().getEnd().getPosition() + length, f.getLocations().getEnd().getPosition());
            }else {
                assertEquals(temp.getLocations().getStart().getPosition() + length, f.getLocations().getStart().getPosition());
                assertEquals(temp.getLocations().getEnd().getPosition() + length, f.getLocations().getEnd().getPosition());
            }
        }
    }
    @Test
    void modifyDNASequenceTest() throws IOException {
        GenBankEditor editor = new GenBankEditor();
        String DNAFile = Files.readString(Path.of("src\\test\\resources\\X81322.gb"));
        BufferedReader bufferedReader = new BufferedReader(new StringReader(DNAFile));
        GenbankSequenceParser parser = new GenbankSequenceParser<>();

        DNASequence sequenceAdd = editor.modify(TestDNASequence, position, DNAFile, DNASequence.class);

        String initSequence = parser.getSequence(bufferedReader, 0);
        assertEquals(initSequence.length(), sequenceAdd.getLength());
        assertEquals(TestDNASequence, sequenceAdd.getSequenceAsString().substring(position - 1, position - 1 + length));
        assertEquals(initSequence.substring(0, position - 1) + initSequence.substring(position - 1 + length),
                sequenceAdd.getSequenceAsString().substring(0, position - 1) + sequenceAdd.getSequenceAsString().substring(position - 1 + length, sequenceAdd.getLength()));

        Map<String, List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>>> initFeatures = parser.getFeatures();
        List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> newFeatures = sequenceAdd.getFeatures();

        assertEquals(initFeatures.size(), newFeatures.size());
        for (var f: newFeatures) {
            var temp= initFeatures.get(f.getDescription()).get(0);
            assertEquals(temp.getLocations().getStart().getPosition(), f.getLocations().getStart().getPosition());
            assertEquals(temp.getLocations().getEnd().getPosition(), f.getLocations().getEnd().getPosition());
        }
    }

    @Test
    void cutDNASequenceTest() throws IOException {
        GenBankEditor editor = new GenBankEditor();
        String DNAFile = Files.readString(Path.of("src\\test\\resources\\X81322.gb"));
        BufferedReader bufferedReader = new BufferedReader(new StringReader(DNAFile));
        GenbankSequenceParser parser = new GenbankSequenceParser<>();

        DNASequence sequenceAdd = editor.cut(length, position, DNAFile, DNASequence.class);

        String initSequence = parser.getSequence(bufferedReader, 0);
        assertEquals(initSequence.length() - length, sequenceAdd.getLength());
        assertEquals(initSequence.substring(0, position - 1) + initSequence.substring(position - 1 + length),
                sequenceAdd.getSequenceAsString());

        Map<String, List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>>> initFeatures = parser.getFeatures();
        List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> newFeatures = sequenceAdd.getFeatures();

        assertEquals(initFeatures.size(), newFeatures.size());
        for (var f: newFeatures) {
            var temp= initFeatures.get(f.getDescription()).get(0);
            if (Objects.equals(f.getDescription(), "source")) {
                assertEquals(temp.getLocations().getStart().getPosition(), f.getLocations().getStart().getPosition());
                assertEquals(temp.getLocations().getEnd().getPosition() - length, f.getLocations().getEnd().getPosition());
            }else {
                assertEquals(position, f.getLocations().getStart().getPosition());
                assertEquals(temp.getLocations().getEnd().getPosition() - length, f.getLocations().getEnd().getPosition());
            }
        }
    }
}
