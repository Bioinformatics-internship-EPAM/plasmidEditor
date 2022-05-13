package com.plasmidEditor.sputnik.uploadServices;

import java.io.InputStream;

import org.biojava.nbio.core.sequence.DNASequence;

public class DNAFileUploadService implements FileUploadService<DNASequence> {

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
