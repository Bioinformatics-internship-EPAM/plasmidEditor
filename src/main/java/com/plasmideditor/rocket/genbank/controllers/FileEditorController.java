package com.plasmideditor.rocket.genbank.controllers;

import com.plasmideditor.rocket.genbank.domains.request.ModificationRequest;
import com.plasmideditor.rocket.genbank.services.EditService;
import com.plasmideditor.rocket.genbank.services.exceptions.GenBankFileEditorException;
import com.plasmideditor.rocket.genbank.services.exceptions.SequenceValidationException;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.plasmideditor.rocket.genbank.configuration.ApiConstants.*;

@Controller
@RequestMapping(value = EDIT_FILE_PATH)
public class FileEditorController {

    private final EditService editService;

    public FileEditorController(EditService editService) {
        this.editService = editService;
    }

    @PostMapping(path = ADD_SEQ_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addSequenceToFile(@RequestBody ModificationRequest request) {
        String response;
        try {
            String fileContent = editService.getFileFromDB(
                    request.getFileRequest().getFileId(),
                    request.getFileRequest().getFileVersion()
            );
            Class sequenceType = editService.getSequenceType(fileContent);
            editService.validateSequence(request.getSequenceInfoRequest().getSequence(), sequenceType);
            AbstractSequence sequence = editService.addGenBankFile(request.getSequenceInfoRequest().getStartPosition(),
                    request.getSequenceInfoRequest().getSequence(),
                    fileContent,
                    sequenceType);
            response = editService.saveSequenceToDB(request.getFileRequest().getFileId(), request.getFileRequest().getFileVersion(), sequence);
        } catch (SequenceValidationException e) {
            return ResponseEntity.badRequest().body("Sequence not suitable for file: expected type is " + e.getType());
        } catch (GenBankFileEditorException e) {
            return ResponseEntity.badRequest().body("Fail to edit file: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = CUT_SEQ_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cutSequence(@RequestBody ModificationRequest request) {
        String response;
        try {
            String fileContent = editService.getFileFromDB(
                    request.getFileRequest().getFileId(),
                    request.getFileRequest().getFileVersion()
            );
            Class sequenceType = editService.getSequenceType(fileContent);
            AbstractSequence sequence = editService.cutGenBankFile(request.getSequenceInfoRequest().getStartPosition(),
                    request.getSequenceInfoRequest().getSequence(),
                    fileContent,
                    sequenceType);
            response = editService.saveSequenceToDB(request.getFileRequest().getFileId(), request.getFileRequest().getFileVersion(), sequence);
        } catch (GenBankFileEditorException e) {
            return ResponseEntity.badRequest().body("Fail to edit file: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = MODIFY_SEQ_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> modifySequence(@RequestBody ModificationRequest request) {
        String response;
        try {
            String fileContent = editService.getFileFromDB(
                    request.getFileRequest().getFileId(),
                    request.getFileRequest().getFileVersion()
            );
            Class sequenceType = editService.getSequenceType(fileContent);
            editService.validateSequence(request.getSequenceInfoRequest().getSequence(), sequenceType);
            AbstractSequence sequence = editService.modifyGenBankFile(request.getSequenceInfoRequest().getStartPosition(),
                    request.getSequenceInfoRequest().getSequence(),
                    fileContent,
                    sequenceType);
            response = editService.saveSequenceToDB(request.getFileRequest().getFileId(), request.getFileRequest().getFileVersion(), sequence);
        } catch (SequenceValidationException e) {
            return ResponseEntity.badRequest().body("Sequence not suitable for file: expected type is " + e.getType());
        } catch (GenBankFileEditorException e) {
            return ResponseEntity.badRequest().body("Fail to edit file: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
