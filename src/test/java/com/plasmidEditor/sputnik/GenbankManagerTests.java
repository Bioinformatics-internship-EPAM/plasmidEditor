package com.plasmidEditor.sputnik;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.junit.jupiter.api.Test;

class GenbankManagerTests {
    @Test
    void readDNAByURLTest() {
        GenbankManager gbManager = new GenbankManager();
        DNASequence dnaSequence = gbManager.readDNAByURL("X81322");
        System.out.format("Sequence(%s,%d)=%s...\n", dnaSequence.getAccession(),
                dnaSequence.getLength(),
                dnaSequence.getSequenceAsString().substring(0, 10));
    }

    @Test
    void readProteinByURLTest() {
        GenbankManager gbManager = new GenbankManager();
        ProteinSequence proteinSequence = gbManager.readProteinByURL("NP_000257");
        System.out.format("Sequence(%s,%d)=%s...\n",
                proteinSequence.getAccession(),
                proteinSequence.getLength(),
                proteinSequence.getSequenceAsString().substring(0, 10));
    }
}
