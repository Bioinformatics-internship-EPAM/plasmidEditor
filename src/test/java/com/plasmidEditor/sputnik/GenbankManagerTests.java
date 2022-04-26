package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.utils.ReaderUtils;
import lombok.SneakyThrows;
import org.biojava.nbio.core.sequence.*;
import org.biojava.nbio.core.sequence.compound.*;
import org.biojava.nbio.core.sequence.io.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class GenbankManagerTests {

    @Test
    void readDNAByURLTest() throws IOException {
        GenbankManager<DNASequence> gbManager = new UrlDNAGenbankManager();
        DNASequence dnaSequence = gbManager.readSequence("X81322");
        checkSequenceX81322(dnaSequence);
    }

    @SneakyThrows
    @Test
    void readProteinByURLTest() throws IOException {
        GenbankManager<ProteinSequence> gbManager = new UrlProteinGenbankManager();
        ProteinSequence proteinSequence = gbManager.readSequence("NP_000257");
        checkSequenceNP_000257(proteinSequence);
    }

    @SneakyThrows
    @Test
    void readDNAFromFileTest() throws IOException {
        GenbankManager<DNASequence> gbManager = new FileDNAGenbankManager();
        DNASequence dnaSequence = gbManager.readSequence("src/test/resources/X81322.gb");
        checkSequenceX81322(dnaSequence);
    }

    @SneakyThrows
    @Test
    void readProteinFromFileTest() throws IOException {
        GenbankManager<ProteinSequence> gbManager = new FileProteinGenbankManager();
        ProteinSequence proteinSequence = gbManager.readSequence("src/test/resources/NP_000257.gb");
        checkSequenceNP_000257(proteinSequence);
    }

    @SneakyThrows
    @Test
    void writeDNAToFileTest() throws IOException {
        Path tmp = Files.createTempFile("dna_", "test");
        String tmpPath = tmp.toFile().getAbsolutePath();
        String id = "TEST";
        String seq = "gaagtagaag";
        DNASequence dnaSequence = (DNASequence) new DNASequenceCreator(DNACompoundSet.getDNACompoundSet())
            .getSequence(seq, 1);
        dnaSequence.setAccession(new AccessionID(id));
        new FileDNAGenbankManager().writeSequence(tmpPath, dnaSequence);
        assertTrue(ReaderUtils.readStringFromFile(tmpPath).contains("ACCESSION   " + id));
        assertTrue(ReaderUtils.readStringFromFile(tmpPath).contains("1 " + seq));
        Files.delete(tmp);
    }

    @SneakyThrows
    @Test
    void writeProteinToFileTest() throws Exception {
        Path tmp = Files.createTempFile("protein_", "test");
        String tmpPath = tmp.toFile().getAbsolutePath();
        String id = "TEST";
        String seq = "gaagtagaag";
        ProteinSequence proteinSequence = (ProteinSequence) new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet())
            .getSequence(seq, 1);
        proteinSequence.setAccession(new AccessionID(id));
        new FileProteinGenbankManager().writeSequence(tmpPath, proteinSequence);
        assertTrue(ReaderUtils.readStringFromFile(tmpPath).contains("ACCESSION   " + id));
        assertTrue(ReaderUtils.readStringFromFile(tmpPath).contains("1 " + seq));
        Files.delete(tmp);
    }

    private void checkSequenceX81322(DNASequence dnaSequence) {
        assertEquals(1499, dnaSequence.getLength());
        String expectedSequence = ReaderUtils.readStringFromFile("src/test/resources/sequenceX81322.txt");
        assertTrue(expectedSequence.equalsIgnoreCase(dnaSequence.getSequenceAsString()));
    }

    private void checkSequenceNP_000257(ProteinSequence proteinSequence) {
        assertEquals(133, proteinSequence.getLength());
        String expectedSequence = ReaderUtils.readStringFromFile("src/test/resources/sequenceNP_000257.txt");
        assertTrue(expectedSequence.equalsIgnoreCase(proteinSequence.getSequenceAsString()));
    }
}
