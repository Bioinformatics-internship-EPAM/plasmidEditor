package com.plasmidEditor.sputnik.uploadServices;

import com.plasmideditor.rocket.genbank.io.protein.FileDNAGenbankManager;
import com.plasmideditor.rocket.web.service.exceptions.FileEditorUploadException;
import org.biojava.nbio.core.sequence.ProteinSequence;

import java.io.File;
import java.util.List;
import java.io.InputStream

public class DNAFileUploadService implements FileUploadService<DNASequence> {

	@Override
    public void uploadFile(InputStream inputstream) throws FileEditorUploadException {
        List<DNASequence> sequenceList;

        try {
            File file = File.createTempFile("dna", ".tmp");
            file.deleteOnExit();
            // Write to tmp file to get accession and version
            inputStream.transferTo(file);

            // If possible to read then the format is correct
            sequenceList = new FileDNAGenbankManager().readSequense(file.getAbsolutePath());
            validateListSize(sequenceList);
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

    private void validateListSize(List<DNASequence> sequences) throws Exception {
        switch (sequences.size()) {
            case 0:
                throw new Exception("File has invalid format.");
            case 1:
                break;
            default:
                throw new Exception("Upload " +
                        sequences.size() + " sequences at once. " +
                        "Upload only one sequence per file."
                );
        }
    }

}
