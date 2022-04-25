package com.plasmidEditor.sputnik;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.*;
import org.springframework.lang.NonNull;

import java.io.*;
import java.util.*;

public class FileDNAGenbankManager implements GenbankManager<DNASequence> {
    @Override
    public DNASequence readSequence(@NonNull String path) {
        try {
            File dnaFile = new File(path);
            Map<String, DNASequence> dnaSequences = GenbankReaderHelper.readGenbankDNASequence(dnaFile);
            return dnaSequences.entrySet().iterator().next().getValue();
        } catch (Exception e) {
            throw new RuntimeException("Can't read sequence from GenBank", e);
        }
    }

    @Override
    public void writeSequence(@NonNull String path, DNASequence sequence) {
        try (ByteArrayOutputStream fragwriter = new ByteArrayOutputStream();
             FileOutputStream outputStream = new FileOutputStream(path)) {
            GenbankWriterHelper.writeNucleotideSequence(fragwriter, Collections.singleton(sequence),
                GenbankWriterHelper.LINEAR_DNA);
            fragwriter.writeTo(outputStream);
        } catch (Exception e) {
            throw new RuntimeException("Can't write sequence to GenBank", e);
        }
    }
}
