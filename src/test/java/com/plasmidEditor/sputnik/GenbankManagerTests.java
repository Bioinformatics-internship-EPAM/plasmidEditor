package com.plasmidEditor.sputnik;

import org.biojava.nbio.core.sequence.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class GenbankManagerTests{
    @Test
    void readDNAByURLTest() {
        GenbankManager gbManager = new DNAGenbankManager();
        DNASequence dnaSequence = (DNASequence) gbManager.readByURL("X81322");
        checkSequenceX81322(dnaSequence);
        checkFeaturesX81322(dnaSequence);
    }

    @Test
    void readProteinByURLTest() {
        GenbankManagerDNA gbManager = new GenbankManagerDNA();
        ProteinSequence proteinSequence = gbManager.readProteinByURL("NP_000257");
        checkSequenceNP_000257(proteinSequence);
        checkFeaturesNP_000257(proteinSequence);
    }

    @Test
    void readDNAFromFileTest() {
        GenbankManagerDNA gbManager = new GenbankManagerDNA();
        DNASequence dnaSequence = gbManager.readDNAFromFile("src/test/resources/X81322.gb");
        checkSequenceX81322(dnaSequence);
        checkFeaturesX81322(dnaSequence);
    }

    @Test
    void readProteinFromFileTest() {
        GenbankManagerDNA gbManager = new GenbankManagerDNA();
        ProteinSequence proteinSequence = gbManager.readProteinFromFile("src/test/resources/NP_000257.gb");
        checkSequenceNP_000257(proteinSequence);
        checkFeaturesNP_000257(proteinSequence);
    }

    @Test
    void writeDNAToFileTest() throws IOException {
        Path tmp = Files.createTempFile("dna_", "test");
        String tmpPath = tmp.toFile().getAbsolutePath();
        GenbankManagerDNA gbManager = new GenbankManagerDNA();
        DNASequence dnaSequence = gbManager.readDNAByURL("X81322");
        gbManager.writeDNAToFile(tmpPath, dnaSequence);
        DNASequence dnaFromFile = gbManager.readDNAFromFile(tmpPath);
        assertEquals(dnaSequence, dnaFromFile);
        checkFeaturesX81322(dnaFromFile);
        Files.delete(tmp);
    }

    @Test
    void writeProteinToFileTest() throws IOException {
        Path tmp = Files.createTempFile("protein_", "test");
        String tmpPath = tmp.toFile().getAbsolutePath();
        GenbankManagerDNA gbManager = new GenbankManagerDNA();
        ProteinSequence proteinSequence = gbManager.readProteinByURL("NP_000257");
        gbManager.writeProteinToFile(tmpPath, proteinSequence);
        ProteinSequence proteinFromFile = gbManager.readProteinFromFile(tmpPath);
        assertEquals(proteinSequence, proteinFromFile);
        checkFeaturesNP_000257(proteinFromFile);
        Files.delete(tmp);
    }


    private void checkFeaturesX81322(DNASequence dnaSequence) {
        assertEquals("Escherichia coli", dnaSequence.getFeatures().get(0).getQualifiers().get("organism").get(0).getValue());
        assertEquals("C", dnaSequence.getFeatures().get(0).getQualifiers().get("strain").get(0).getValue());
        assertEquals("genomic DNA", dnaSequence.getFeatures().get(0).getQualifiers().get("mol_type").get(0).getValue());
        assertEquals("hpcC", dnaSequence.getFeatures().get(1).getQualifiers().get("gene").get(0).getValue());
        assertEquals("5-carboxymethyl-2-hydroxymuconate semialdehyde dehydrogenase", dnaSequence.getFeatures().get(1).getQualifiers().get("product").get(0).getValue());
        assertEquals("CAA57102.1", dnaSequence.getFeatures().get(1).getQualifiers().get("protein_id").get(0).getValue());
        String translation = "MKKVNHWINGKNVAGNDYFLTTNPATGEVLADVASGGEAEINQA" +
            "VATAKEAFPKWANLPMKERARLMRRLGDLIDQNVPEIAAMETADTGLPIHQTKNVLIP" +
            "RASHNFEFFAEVCQQMNGKTYPVDDKMLNYTLVQPVGVCALVSPWNVPFMTATWKVAP" +
            "CLALGITAVLKMSELSPLTADRLGELALEAGIPAGVLNVVQGYGATAGDALVRHHDVR" +
            "AVSFTGGTATGRNIMKNAGLKKYSMELGGKSPVLIFEDADIERALDAALFTIFSINGE" +
            "RCTAGSRIFIQQSIYPEFVKFAERANRVRVGDPTDPNTQVGALISQQHWEKVSGYIRL" +
            "GIEEGATLLAGGPDKPSDLPAHLKGGNFLRPTVLADVDNRMRVAQEEIFGPVACLLPF" +
            "KDEAEALRLANDVEYGLASYIWTQDVSKVLRLARGIEAGMVFVNTQFVRDLRHAFGGV" +
            "KPRTGREGGGYSSKCSRK";
        assertEquals(translation, dnaSequence.getFeatures().get(1).getQualifiers().get("translation").get(0).getValue());
    }

    private void checkFeaturesNP_000257(ProteinSequence proteinSequence) {
        assertEquals("NDP", proteinSequence.getFeatures().get(0).getQualifiers().get("gene").get(0).getValue());
        assertEquals("EVR2; FEVR; ND", proteinSequence.getFeatures().get(0).getQualifiers().get("gene_synonym").get(0).getValue());
        assertEquals("NM_000266.4:295..696", proteinSequence.getFeatures().get(0).getQualifiers().get("coded_by").get(0).getValue());
        assertEquals("Homo sapiens", proteinSequence.getFeatures().get(1).getQualifiers().get("organism").get(0).getValue());
        assertEquals("X", proteinSequence.getFeatures().get(1).getQualifiers().get("chromosome").get(0).getValue());
        assertEquals("Xp11.3", proteinSequence.getFeatures().get(1).getQualifiers().get("map").get(0).getValue());
    }

    private void checkSequenceX81322(DNASequence dnaSequence) {
        assertEquals(1499, dnaSequence.getLength());
        String expectedSequence = readStringFromFile("src/test/resources/sequenceX81322.txt");
        assertTrue(expectedSequence.equalsIgnoreCase(dnaSequence.getSequenceAsString()));
    }

    private void checkSequenceNP_000257(ProteinSequence proteinSequence) {
        assertEquals(133, proteinSequence.getLength());
        String expectedSequence = readStringFromFile("src/test/resources/sequenceNP_000257.txt");
        assertTrue(expectedSequence.equalsIgnoreCase(proteinSequence.getSequenceAsString()));
    }

    private static String readStringFromFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
