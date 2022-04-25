package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.utils.ReaderUtils;
import org.biojava.nbio.core.sequence.*;
import org.biojava.nbio.core.sequence.compound.*;
import org.biojava.nbio.core.sequence.io.*;
import org.junit.jupiter.api.Test;

import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class GenbankManagerTests {
    @Test
    void readDNAByURLTest() {
        GenbankManager<DNASequence> gbManager = new UrlDNAGenbankManager();
        try {
            DNASequence dnaSequence = gbManager.readSequence("X81322");
            checkSequenceX81322(dnaSequence);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void readProteinByURLTest() {
        GenbankManager<ProteinSequence> gbManager = new UrlProteinGenbankManager();
        try {
            ProteinSequence proteinSequence = gbManager.readSequence("NP_000257");
            checkSequenceNP_000257(proteinSequence);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void readDNAFromFileTest() {
        GenbankManager<DNASequence> gbManager = new FileDNAGenbankManager();
        try {
            DNASequence dnaSequence = gbManager.readSequence("src/test/resources/X81322.gb");
            checkSequenceX81322(dnaSequence);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void readProteinFromFileTest() {
        GenbankManager<ProteinSequence> gbManager = new FileProteinGenbankManager();
        try {
            ProteinSequence proteinSequence = gbManager.readSequence("src/test/resources/NP_000257.gb");
            checkSequenceNP_000257(proteinSequence);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void writeDNAToFileTest() {
        try {
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
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void writeProteinToFileTest() {
        try {
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
        } catch (Exception e) {
            fail(e);
        }
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
