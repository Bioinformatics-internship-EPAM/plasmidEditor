package com.plasmidEditor.sputnik;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.*;
import org.biojava.nbio.core.sequence.io.*;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;

import java.io.*;
import java.util.*;

public class DNAGenbankManager implements GenbankManager<DNASequence>{
    @Override
    public DNASequence readByURL(String accession) {
        try {
            GenbankProxySequenceReader<NucleotideCompound> genbankDNAReader =
                new GenbankProxySequenceReader<>("/tmp", accession,
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

    @Override
    public DNASequence readFromFile(String path) {
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

    @Override
    public void writeToFile(String fileName, DNASequence sequence) {
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
}
