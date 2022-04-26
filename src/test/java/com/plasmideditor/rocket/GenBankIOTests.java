package com.plasmideditor.rocket;

import com.plasmideditor.rocket.genbank.io.dna.GenBankDNAFileReader;
import com.plasmideditor.rocket.genbank.io.dna.GenBankDNAUrlReader;
import com.plasmideditor.rocket.genbank.io.dna.GenBankDNAWriter;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankFileReaderException;
import com.plasmideditor.rocket.genbank.io.protein.GenBankProteinFileReader;
import com.plasmideditor.rocket.genbank.io.protein.GenBankProteinUrlReader;
import com.plasmideditor.rocket.genbank.io.protein.GenBankProteinWriter;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenBankIOTests {

    @Test
    public void readDNAFromURLTest() {
        assertDoesNotThrow(() -> {
            /* https://www.ncbi.nlm.nih.gov/nuccore/2XE0_D?report=genbank */
            List<DNASequence> dnaSequences = new GenBankDNAUrlReader().read_sequence("2XE0_D");
            DNASequence dnaSequence = dnaSequences.get(0);

            assertEquals("2XE0_D", dnaSequence.getAccession().toString());
            assertEquals(24, dnaSequence.getLength());
            assertEquals("TCTGGCTGAG", dnaSequence.getSequenceAsString().substring(0, 10));
            assertEquals("Homo sapiens", dnaSequence.getFeatures().get(0).getQualifiers().get("organism").get(0).getValue());
        });
    }

    @Test
    public void readProteinFromURLTest() {
        assertDoesNotThrow(() -> {
            /* https://www.ncbi.nlm.nih.gov/protein/NP_001317186.1 */
            List<ProteinSequence> proteinSequences = new GenBankProteinUrlReader().read_sequence("NP_001317186");
            ProteinSequence proteinSequence = proteinSequences.get(0);

            assertEquals("NP_001317186", proteinSequence.getAccession().toString());
            assertEquals(915, proteinSequence.getLength());
            assertEquals("MASFSMDCSP", proteinSequence.getSequenceAsString().substring(0, 10));
        });
    }

    @Test
    public void readDNAFromFileTest() {
        assertDoesNotThrow(() -> {
            List<DNASequence> dnaSequences = new GenBankDNAFileReader().read_sequence("src/test/resources/BI431008.gb");
            assertEquals(1, dnaSequences.size());
            DNASequence dnaSequence = dnaSequences.get(0);

            assertEquals("BI431008", dnaSequence.getAccession().toString());
            assertEquals(212, dnaSequence.getLength());
            assertEquals("GTGACTGCGC", dnaSequence.getSequenceAsString().substring(0, 10));
            assertEquals("Mesocricetus auratus", dnaSequence.getFeatures().get(0).getQualifiers().get("organism").get(0).getValue());
        });
    }

    @Test
    public void readProteinFromFileTest() {
        assertDoesNotThrow(() -> {
            List<ProteinSequence> proteinSequences = new GenBankProteinFileReader().read_sequence("src/test/resources/3MJ8_A.gb");
            assertEquals(1, proteinSequences.size());
            ProteinSequence proteinSequence = proteinSequences.get(0);

            assertEquals("3MJ8_A", proteinSequence.getAccession().toString());
            assertEquals(213, proteinSequence.getLength());
            assertEquals("SYTLTQPPLV", proteinSequence.getSequenceAsString().substring(0, 10));
            assertEquals("Cricetulus migratorius", proteinSequence.getFeatures().get(0).getQualifiers().get("organism").get(0).getValue());
        });
    }

    @Test
    public void writeDNAToFileTest() {
        String initialFile = "src/test/resources/BI431008.gb";
        String generatedFile = "src/test/resources/generated_BI431008.gb";

        assertDoesNotThrow(() -> {
            List<DNASequence> dnaSequencesInitial = new GenBankDNAFileReader().read_sequence(initialFile);
            new GenBankDNAWriter().writeToFile(dnaSequencesInitial, generatedFile);
            List<DNASequence> dnaSequencesGenerated = new GenBankDNAFileReader().read_sequence(generatedFile);

            assertEquals(1, dnaSequencesInitial.size());
            assertEquals(1, dnaSequencesGenerated.size());

            assertEquals(dnaSequencesInitial.get(0), dnaSequencesGenerated.get(0));
        });
    }

    @Test
    public void writeProteinToFileTest() {
        String initialFile = "src/test/resources/3MJ8_A.gb";
        String generatedFile = "src/test/resources/generated_3MJ8_A.gb";

        assertDoesNotThrow(() -> {
            List<ProteinSequence> proteinSequencesInitial = new GenBankProteinFileReader().read_sequence(initialFile);
            new GenBankProteinWriter().writeToFile(proteinSequencesInitial, generatedFile);
            List<ProteinSequence> proteinSequencesGenerated = new GenBankProteinFileReader().read_sequence(generatedFile);

            assertEquals(1, proteinSequencesInitial.size());
            assertEquals(1, proteinSequencesGenerated.size());

            assertEquals(proteinSequencesInitial.get(0), proteinSequencesGenerated.get(0));
        });
    }

    @Test
    public void exceptionsWhenReadFromNonExistentFile() {
        assertThrows(GenBankFileReaderException.class,
                () -> {
                    String nonExistentFile = "nonExistentFile.gb";
                    new GenBankProteinFileReader().read_sequence(nonExistentFile);
                    new GenBankDNAFileReader().read_sequence(nonExistentFile);
                });
    }
}
