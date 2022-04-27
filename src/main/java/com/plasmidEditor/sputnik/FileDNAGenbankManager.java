package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.utils.*;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.*;
import org.springframework.lang.NonNull;

import java.io.*;
import java.util.*;

public class FileDNAGenbankManager implements GenbankManager<DNASequence> {
    @Override
    public DNASequence readSequence(@NonNull String path) throws ReadGenbankFileException {
        try {
            File dnaFile = new File(path);
            Map<String, DNASequence> dnaSequences = GenbankReaderHelper.readGenbankDNASequence(dnaFile);
            return getOnlySequenceFromMap(dnaSequences);
        } catch (Exception e) {
            throw new ReadGenbankFileException(path, e);
        }
    }

    @Override
    public void writeSequence(@NonNull String path, DNASequence sequence) throws WriteGenbankFileException {
        try (ByteArrayOutputStream fragwriter = new ByteArrayOutputStream();
             FileOutputStream outputStream = new FileOutputStream(path)) {
            GenbankWriterHelper.writeNucleotideSequence(fragwriter, Collections.singleton(sequence),
                GenbankWriterHelper.LINEAR_DNA);
            fragwriter.writeTo(outputStream);
        } catch (Exception e) {
            throw new WriteGenbankFileException(path, e);
        }
    }

    private DNASequence getOnlySequenceFromMap(Map<String, DNASequence> dnaSequences) {
        if (dnaSequences.size() == 1) {
            return dnaSequences.values().iterator().next();
        }
        throw new IllegalArgumentException("Can't read sequence from file");
    }
}
