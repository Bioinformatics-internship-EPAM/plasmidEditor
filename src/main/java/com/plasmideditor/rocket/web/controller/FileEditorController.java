package com.plasmideditor.rocket.web.controller;

import com.plasmideditor.rocket.web.domains.request.ModificationRequest;
import com.plasmideditor.rocket.web.service.modifications.AddModification;
import com.plasmideditor.rocket.web.service.modifications.CutModification;
import com.plasmideditor.rocket.web.service.EditService;
import com.plasmideditor.rocket.web.service.modifications.ModifyModification;
import com.plasmideditor.rocket.web.service.modifications.SequenceModification;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileEditorException;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileNotFound;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import com.plasmideditor.rocket.web.service.exceptions.UnknownSequenceType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.plasmideditor.rocket.web.configuration.ApiConstants.*;

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
    public ResponseEntity<String> addSequenceToFile(@RequestBody ModificationRequest request) throws GenBankFileEditorException, UnknownSequenceType, GenBankFileNotFound, SequenceValidationException {
        SequenceModification service = new AddModification();
        String response = editService.modifySequence(request, service).getSequenceAsString();
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = CUT_SEQ_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cutSequence(@RequestBody ModificationRequest request) throws GenBankFileNotFound, GenBankFileEditorException, UnknownSequenceType, SequenceValidationException {
        SequenceModification service = new CutModification();
        String response = editService.modifySequence(request, service).getSequenceAsString();

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = MODIFY_SEQ_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> modifySequence(@RequestBody ModificationRequest request) throws GenBankFileNotFound, GenBankFileEditorException, UnknownSequenceType, SequenceValidationException {
        SequenceModification service = new ModifyModification();
        String response = editService.modifySequence(request, service).getSequenceAsString();

        return ResponseEntity.ok(response);
    }
}
