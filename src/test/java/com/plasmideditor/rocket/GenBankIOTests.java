package com.plasmideditor.rocket;

import com.plasmideditor.rocket.genbank.io.GenBankDNAReader;
import com.plasmideditor.rocket.genbank.io.GenBankDNAWriter;
import com.plasmideditor.rocket.genbank.io.GenBankProteinReader;
import com.plasmideditor.rocket.genbank.io.GenBankProteinWriter;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenBankIOTests {

    @Test
    public void readDNAFromURLTest() {
        /* https://www.ncbi.nlm.nih.gov/nuccore/2XE0_D?report=genbank */
        DNASequence dnaSequence = new GenBankDNAReader().readFromURL("2XE0_D");

        assertEquals("2XE0_D", dnaSequence.getAccession().toString());
        assertEquals(24, dnaSequence.getLength());
        assertEquals("TCTGGCTGAG", dnaSequence.getSequenceAsString().substring(0, 10));
        assertEquals("Homo sapiens", dnaSequence.getFeatures().get(0).getQualifiers().get("organism").get(0).getValue());
    }

    @Test
    public void readProteinFromURLTest() {
        /* https://www.ncbi.nlm.nih.gov/protein/NP_001317186.1 */
        ProteinSequence proteinSequence = new GenBankProteinReader().readFromURL("NP_001317186");

        assertEquals("NP_001317186", proteinSequence.getAccession().toString());
        assertEquals(915, proteinSequence.getLength());
        assertEquals("MASFSMDCSP", proteinSequence.getSequenceAsString().substring(0, 10));
    }

    @Test
    public void readDNAFromFileTest() {
        ArrayList<DNASequence> dnaSequences = new GenBankDNAReader().readFromFile("src/test/resources/BI431008.gb");
        assertEquals(1, dnaSequences.size());
        DNASequence dnaSequence = dnaSequences.get(0);

        assertEquals("BI431008", dnaSequence.getAccession().toString());
        assertEquals(212, dnaSequence.getLength());
        assertEquals("GTGACTGCGC", dnaSequence.getSequenceAsString().substring(0, 10));
        assertEquals("Mesocricetus auratus", dnaSequence.getFeatures().get(0).getQualifiers().get("organism").get(0).getValue());
    }

    @Test
    public void readProteinFromFileTest() {
        ArrayList<ProteinSequence> proteinSequences = new GenBankProteinReader().readFromFile("src/test/resources/3MJ8_A.gb");
        assertEquals(1, proteinSequences.size());
        ProteinSequence proteinSequence = proteinSequences.get(0);

        assertEquals("3MJ8_A", proteinSequence.getAccession().toString());
        assertEquals(213, proteinSequence.getLength());
        assertEquals("SYTLTQPPLV", proteinSequence.getSequenceAsString().substring(0, 10));
        assertEquals("Cricetulus migratorius", proteinSequence.getFeatures().get(0).getQualifiers().get("organism").get(0).getValue());
    }

    @Test
    public void writeDNAToFileTest() {
        String initialFile = "src/test/resources/BI431008.gb";
        String generatedFile = "src/test/resources/generated_BI431008.gb";
        
        ArrayList<DNASequence> dnaSequencesInitial = new GenBankDNAReader().readFromFile(initialFile);
        new GenBankDNAWriter().writeToFile(dnaSequencesInitial, generatedFile);
        ArrayList<DNASequence> dnaSequencesGenerated = new GenBankDNAReader().readFromFile(generatedFile);

        assertEquals(1, dnaSequencesInitial.size());
        assertEquals(1, dnaSequencesGenerated.size());

        assertEquals(dnaSequencesInitial.get(0), dnaSequencesGenerated.get(0));
    }

    @Test
    public void writeProteinToFileTest() {
        String initialFile = "src/test/resources/3MJ8_A.gb";
        String generatedFile = "src/test/resources/generated_3MJ8_A.gb";

        ArrayList<ProteinSequence> proteinSequencesInitial = new GenBankProteinReader().readFromFile(initialFile);
        new GenBankProteinWriter().writeToFile(proteinSequencesInitial, generatedFile);
        ArrayList<ProteinSequence> proteinSequencesGenerated = new GenBankProteinReader().readFromFile(generatedFile);

        assertEquals(1, proteinSequencesInitial.size());
        assertEquals(1, proteinSequencesGenerated.size());

        assertEquals(proteinSequencesInitial.get(0), proteinSequencesGenerated.get(0));
    }
}
