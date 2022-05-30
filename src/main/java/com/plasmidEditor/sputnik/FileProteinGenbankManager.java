package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.exceptions.*;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.*;
import org.springframework.lang.NonNull;

import java.io.*;
import java.util.*;

public class FileProteinGenbankManager implements GenbankManager<ProteinSequence> {
    @Override
    public ProteinSequence readSequence(@NonNull String path) {
        try {
            File protFile = new File(path);
            Map<String, ProteinSequence> protSequences = GenbankReaderHelper.readGenbankProteinSequence(protFile);
            return getOnlySequenceFromMap(protSequences);
        } catch (Exception e) {
            throw new ReadGenbankFileException(path, e);
        }
    }

    @Override
    public void writeSequence(@NonNull String path, ProteinSequence sequence) {
        try (ByteArrayOutputStream fragwriter = new ByteArrayOutputStream();
             FileOutputStream outputStream = new FileOutputStream(path)) {
            GenbankWriterHelper.writeProteinSequence(fragwriter, Collections.singleton(sequence));
            fragwriter.writeTo(outputStream);
        } catch (Exception e) {
            throw new WriteGenbankFileException(path, e);
        }
    }

    private ProteinSequence getOnlySequenceFromMap(Map<String, ProteinSequence> protSequences) {
        int maxSeqCountInFile = 1;
        if (protSequences.size() == maxSeqCountInFile) {
            return protSequences.values().iterator().next();
        }
        throw new ReadGenbankFileException();
    }
}