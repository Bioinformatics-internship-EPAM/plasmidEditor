package com.plasmidEditor.sputnik.editor;

import com.plasmidEditor.sputnik.exceptions.ValidationParametersException;
import com.plasmidEditor.sputnik.repositories.GenBankRepository;
import com.plasmidEditor.sputnik.services.GenBankService;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenBankEditor {
    private final GenBankService genBankService;

    @Autowired
    public GenBankEditor(GenBankService genBankService) {
        this.genBankService = genBankService;
    }

    public <S extends AbstractSequence<C>, C extends Compound> S add(EditorParameters parameters) {
        return new AddMethod().edit(
                parameters.getSubsequence(),
                parameters.getPosition(),
                getFileContent(parameters.getAccession(), parameters.getVersion()),
                0);
    }

    public <S extends AbstractSequence<C>, C extends Compound> S modify(EditorParameters parameters) {
        return new ModifyMethod().edit(
                parameters.getSubsequence(),
                parameters.getPosition(),
                getFileContent(parameters.getAccession(), parameters.getVersion()),
                0);
    }

    public <S extends AbstractSequence<C>, C extends Compound> S cut(EditorParameters parameters) {
        return new CutMethod().edit(
                null,
                parameters.getPosition(),
                getFileContent(parameters.getAccession(), parameters.getVersion()),
                parameters.getCutSize());
    }

    private String getFileContent(String accession, String version) {
        return genBankService.get(accession, version).getFile();
    }

    private void validateParameters(EditorParameters parameters) {
        if (parameters.getPosition() < 0) {
            throw new ValidationParametersException("Error: position is negative");
        }
        if (parameters.getAccession() == null || parameters.getAccession().isEmpty()) {
            throw new ValidationParametersException("Error: accession is empty");
        }
        if (parameters.getVersion() == null || parameters.getVersion().isEmpty()) {
            throw new ValidationParametersException("Error: version is empty");
        }
        if (parameters.getSubsequence() == null) {
            if (parameters.getCutSize() <= 0) {
                throw new ValidationParametersException("Error: cut size is negative or 0");
            }
        } else if (parameters.getSubsequence().isEmpty()) {
            throw new ValidationParametersException("Error: subsequence is empty");
        }
    }
}
