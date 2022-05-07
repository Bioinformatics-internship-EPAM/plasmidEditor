package com.plasmideditor.rocket.genbank.services;

import com.plasmideditor.rocket.genbank.domains.RaketaDNASequence;
import com.plasmideditor.rocket.genbank.domains.RaketaLocation;
import com.plasmideditor.rocket.genbank.domains.request.FileRequest;
import com.plasmideditor.rocket.genbank.domains.request.SequenceInfoRequest;
import com.plasmideditor.rocket.genbank.io.dna.GenBankDNAWriter;
import com.plasmideditor.rocket.genbank.io.exceptions.GenBankWriterException;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.location.template.AbstractLocation;
import org.biojava.nbio.core.sequence.location.template.Point;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DNAFileEditorServiceImpl implements FileEditorService<DNASequence> {

    public void readFileFromDb(FileRequest fileRequest) {

    }

    public boolean validateSequence(String sequence) {
        return sequence.matches("[AaTtGgCc]*");
    }

    public DNASequence addSequence(SequenceInfoRequest sequenceInfoRequest, DNASequence dnaSequence) {
        String initialSequence = dnaSequence.getSequenceAsString();
        String updatedSequence = addNewSequenceToInitial(sequenceInfoRequest, initialSequence);
        RaketaDNASequence modifiedDnaSequence = new RaketaDNASequence(dnaSequence, updatedSequence);

        List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> features = dnaSequence.getFeatures();
        updateFeatures(sequenceInfoRequest, modifiedDnaSequence, features);
        return modifiedDnaSequence;
    }

    private void updateFeatures(SequenceInfoRequest sequenceInfoRequest, RaketaDNASequence modifiedDnaSequence, List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> features) {
        for (FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound> feature : features) {
            AbstractLocation locations = feature.getLocations();
            Point startPoint = locations.getStart();
            Point endPoint = locations.getEnd();
            if (isSeqStartPositionBetweenFeatureStartEnd(sequenceInfoRequest.getStartPosition(), startPoint.getPosition(), endPoint.getPosition())) {
                int updatedEndPosition = endPoint.getPosition() + sequenceInfoRequest.getSequence().length();
                feature.setLocation(new RaketaLocation(startPoint.getPosition(), updatedEndPosition));
                log.debug("Update feature position: start={}, and={}", startPoint.getPosition(), updatedEndPosition);
            } else if (isSeqBeforeFeatureStart(sequenceInfoRequest.getStartPosition(), startPoint.getPosition())) {
                int updatedStartPosition = startPoint.getPosition() + sequenceInfoRequest.getSequence().length();
                int updatedEndPosition = endPoint.getPosition() + sequenceInfoRequest.getSequence().length();
                feature.setLocation(new RaketaLocation(updatedStartPosition, updatedEndPosition));
                log.debug("Update feature position: start={}, and={}", updatedStartPosition, updatedEndPosition);
            }
            modifiedDnaSequence.addFeature(feature);
        }
    }


    public void writeToFile(DNASequence sequence) {
        try {
            String generatedFile = "src/test/resources/generated_3MJ8_test.gb";
            new GenBankDNAWriter().writeToFile(List.of(sequence), generatedFile);
        } catch (GenBankWriterException e) {
            throw new RuntimeException(e);
        }
    }

    private String addNewSequenceToInitial(SequenceInfoRequest sequenceInfoRequest, String sequence) {
        return new StringBuffer(sequence)
                .insert(sequenceInfoRequest.getStartPosition(), sequenceInfoRequest.getSequence())
                .toString();
    }

    private boolean isSeqStartPositionBetweenFeatureStartEnd(Integer startPosition, Integer featureStart, Integer featureEnd) {
        return startPosition >= featureStart && startPosition <= featureEnd;
    }

    private boolean isSeqBeforeFeatureStart(Integer startPosition, Integer featureStart) {
        return startPosition < featureStart;
    }


}
