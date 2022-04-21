package com.plasmidEditor.sputnik;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;

public class GenbankManager {
    public DNASequence readDNAByURL(String accession) {
        try {
            GenbankProxySequenceReader<NucleotideCompound> genbankDNAReader =
                    new GenbankProxySequenceReader<NucleotideCompound>("/tmp", accession,
                            DNACompoundSet.getDNACompoundSet());
            DNASequence dnaSequence = new DNASequence(genbankDNAReader);
            genbankDNAReader.getHeaderParser().parseHeader(genbankDNAReader.getHeader(), dnaSequence);
            return dnaSequence;
        }
        catch (Exception exc) {
            System.err.println("Unable to get Genbank file with accession " + accession + " by URL");
            exc.printStackTrace();
        }
        return null;
    }

    public ProteinSequence readProteinByURL(String accession) {
        try {
            GenbankProxySequenceReader<AminoAcidCompound> genbankProteinReader =
                    new GenbankProxySequenceReader<AminoAcidCompound>("/tmp", accession,
                            AminoAcidCompoundSet.getAminoAcidCompoundSet());

            ProteinSequence proteinSequence = new ProteinSequence(genbankProteinReader);
            genbankProteinReader.getHeaderParser().parseHeader(
                    genbankProteinReader.getHeader(), proteinSequence);
            return proteinSequence;
        }
        catch (Exception exc) {
            System.err.println("Unable to get Genbank file with accession " + accession + " by URL");
            exc.printStackTrace();
        }
        return null;
    }

    public DNASequence readDNAFromFile(String path) {
        return null;
    }

    public ProteinSequence readProteinFromFile(String path) {
        return null;
    }

    public void writeDNAToFile(String fileName, DNASequence sequence) {

    }

    public void writeProteinToFile(String fileName, ProteinSequence sequence) {

    }
}
