package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.genbank.io.dna.GenBankDNAFileReader;
import com.plasmideditor.rocket.web.service.exceptions.FileEditorUploadException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class DNAFileEditorService implements FileEditorService<DNASequence> {
    @Override
    public void uploadFile(MultipartFile multipartFile) throws FileEditorUploadException {
        List<DNASequence> sequenceList;

        try {
            File file = File.createTempFile("dna", ".tmp");
            file.deleteOnExit();
            // Write to tmp file to get accession and version
            multipartFile.transferTo(file);

            // If possible to read then the format is correct
            sequenceList = new GenBankDNAFileReader().read_sequence(file.getAbsolutePath());

            if (sequenceList.size() > 1)
                throw new Exception("Upload " +
                        sequenceList.size() + " sequences at once. " +
                        "Upload only one sequence per file."
                );
        } catch (Exception e) {
            throw new FileEditorUploadException(e.getMessage(), e);
        }

        for (DNASequence sequence : sequenceList) {
            String accession = sequence.getAccession().getID();
            int version = sequence.getAccession().getVersion();

//             if (genBankService.getByAccessionVersion(accession, version) == null) {
//                 genBankService.upload(accession, version, new String(multipartFile.getBytes()));
//             }
            System.out.println("Upload " + accession + "." + version);
        }
    }
}
