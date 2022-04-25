package com.plasmidEditor.sputnik;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.*;
import org.biojava.nbio.core.sequence.io.*;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;

import java.io.*;
import java.util.*;

public class ProteinGenbankManager implements GenbankManager<ProteinSequence> {
    @Override
    public ProteinSequence readByURL(String accession) {
        try {
            GenbankProxySequenceReader<AminoAcidCompound> genbankProteinReader =
                new GenbankProxySequenceReader<>("/tmp", accession,
                    AminoAcidCompoundSet.getAminoAcidCompoundSet());
            ProteinSequence proteinSequence = new ProteinSequence(genbankProteinReader);
            genbankProteinReader.getHeaderParser().parseHeader(
                genbankProteinReader.getHeader(), proteinSequence);
            return proteinSequence;
        } catch (Exception exc) {
            System.err.println("Unable to get Genbank file with accession " + accession + " by URL");
            exc.printStackTrace();
        }
        return null;
    }

    @Override
    public ProteinSequence readFromFile(String path) {
        File protFile = new File(path);
        try {
            Map<String, ProteinSequence> protSequences =
                GenbankReaderHelper.readGenbankProteinSequence(protFile);
            return protSequences.entrySet().iterator().next().getValue();
        } catch (Exception e) {
            System.err.println("Unable to read data from file " + path);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void writeToFile(String fileName, ProteinSequence sequence) {
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
