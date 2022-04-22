package com.plasmidEditor.sputnik;

import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.io.GenbankWriterHelper;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import java.io.*;

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
        File dnaFile = new File(path);
        try {
            LinkedHashMap<String, DNASequence> dnaSequences =
                    GenbankReaderHelper.readGenbankDNASequence( dnaFile );
            return dnaSequences.entrySet().iterator().next().getValue();
        }
        catch (Exception exc) {
            System.err.println("Unable to read Genbank file " + path);
            exc.printStackTrace();
        }
        return null;
    }

    public ProteinSequence readProteinFromFile(String path) {
        File protFile = new File(path);
        try {
            LinkedHashMap<String, ProteinSequence> protSequences =
                    GenbankReaderHelper.readGenbankProteinSequence(protFile);
            return protSequences.entrySet().iterator().next().getValue();
        } catch (Exception e) {
            System.err.println("Unable to read data from file " + path);
            e.printStackTrace();
        }
        return null;
    }

    public void writeDNAToFile(String fileName, DNASequence sequence) {
        ByteArrayOutputStream fragwriter = new ByteArrayOutputStream();
        try {
            GenbankWriterHelper.writeNucleotideSequence(fragwriter, Collections.singleton(sequence),
                    GenbankWriterHelper.LINEAR_DNA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find file " + fileName);
            e.printStackTrace();
        }
        try {
            fragwriter.writeTo(out);
            out.close();
        } catch (IOException e) {
            System.err.println("Unable to write DNA data to file");
            e.printStackTrace();
        }
    }

    public void writeProteinToFile(String fileName, ProteinSequence sequence) {
        ByteArrayOutputStream fragwriter = new ByteArrayOutputStream();
        try {
            GenbankWriterHelper.writeProteinSequence(fragwriter, Collections.singleton(sequence));
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find file " + fileName);
            e.printStackTrace();
        }
        try {
            fragwriter.writeTo(out);
            out.close();
        } catch (IOException e) {
            System.err.println("Unable to write protein data to file");
            e.printStackTrace();
        }
    }
}
