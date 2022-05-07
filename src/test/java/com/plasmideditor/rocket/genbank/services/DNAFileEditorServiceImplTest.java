package com.plasmideditor.rocket.genbank.services;

import com.plasmideditor.rocket.genbank.domains.request.SequenceInfoRequest;
import com.plasmideditor.rocket.genbank.io.dna.GenBankDNAFileReader;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankFileReaderException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DNAFileEditorServiceImplTest {
    private static String TEST_DNA_FILE_PATH = "src/test/resources/BI431008.gb";
    private static String TEST_SEQUENCE = "AAAAAAAAAA";

    @Test
    public void testAddSequence() throws GenBankFileReaderException {
        DNAFileEditorServiceImpl dnaService = new DNAFileEditorServiceImpl();
        DNASequence dnaSequencesInitial = new GenBankDNAFileReader().read_sequence(TEST_DNA_FILE_PATH).get(0);
        List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> initialFeatures = dnaSequencesInitial.getFeatures();

        DNASequence modifiedDnaSequence =
                dnaService.addSequence(new SequenceInfoRequest(1, TEST_SEQUENCE), dnaSequencesInitial);
        int expectedLength = dnaSequencesInitial.getLength() + TEST_SEQUENCE.length();
        assertEquals(expectedLength, modifiedDnaSequence.getLength());

        List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> modifiedFeatures = modifiedDnaSequence.getFeatures();
        assertEquals(initialFeatures.size(), modifiedFeatures.size());

        assertEquals(initialFeatures.get(0).getDescription(), modifiedFeatures.get(0).getDescription());
        int expectedStart = 1;
        int expectedEnd = 222;
        assertEquals(expectedStart, modifiedFeatures.get(0).getLocations().getStart().getPosition());
        assertEquals(expectedEnd, modifiedFeatures.get(0).getLocations().getEnd().getPosition());
    }

}