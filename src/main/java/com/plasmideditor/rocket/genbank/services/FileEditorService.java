package com.plasmideditor.rocket.genbank.services;

import com.plasmideditor.rocket.genbank.domains.request.FileRequest;
import com.plasmideditor.rocket.genbank.domains.request.SequenceInfoRequest;
import org.biojava.nbio.core.sequence.DNASequence;
import org.springframework.stereotype.Service;

@Service
public interface FileEditorService<T> {
    DNASequence addSequence(SequenceInfoRequest sequenceInfoRequest, DNASequence dnaSequence);

    void readFileFromDb(FileRequest fileRequest);
    boolean validateSequence(String sequence);
}
