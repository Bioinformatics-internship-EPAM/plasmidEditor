package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.utils.ReadGenbankFileException;
import com.plasmidEditor.sputnik.utils.WriteGenbankFileException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.*;
import org.springframework.lang.NonNull;

import java.io.*;
import java.util.*;

public class FileProteinGenbankManager implements GenbankManager<ProteinSequence> {
    @Override
    public ProteinSequence readSequence(@NonNull String path) throws ReadGenbankFileException {
        try {
            File protFile = new File(path);
            Map<String, ProteinSequence> protSequences = GenbankReaderHelper.readGenbankProteinSequence(protFile);
            return protSequences.entrySet().iterator().next().getValue();
        } catch (Exception e) {
            throw new ReadGenbankFileException(path, e);
        }
    }

    @Override
    public void writeSequence(@NonNull String path, ProteinSequence sequence) throws WriteGenbankFileException {
        try (ByteArrayOutputStream fragwriter = new ByteArrayOutputStream();
             FileOutputStream outputStream = new FileOutputStream(path)) {
            GenbankWriterHelper.writeProteinSequence(fragwriter, Collections.singleton(sequence));
            fragwriter.writeTo(outputStream);
        } catch (Exception e) {
            throw new WriteGenbankFileException(path, e);
        }
    }
}