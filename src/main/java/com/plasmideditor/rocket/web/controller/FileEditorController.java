package com.plasmideditor.rocket.web.controller;

import com.plasmideditor.rocket.web.domains.request.ModificationRequest;
import com.plasmideditor.rocket.web.service.exceptions.*;
import com.plasmideditor.rocket.web.service.modifications.AddModification;
import com.plasmideditor.rocket.web.service.modifications.CutModification;
import com.plasmideditor.rocket.web.service.EditService;
import com.plasmideditor.rocket.web.service.modifications.ModifyModification;
import com.plasmideditor.rocket.web.service.modifications.SequenceModification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    public FileEditorController(EditService editService) {
        this.editService = editService;
    }

    @PostMapping(path = ADD_SEQ_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addSequenceToFile(@RequestBody ModificationRequest request) throws GenBankFileEditorException, UnknownSequenceType, GenBankFileNotFound, SequenceValidationException, RequestBodyValidationException {
        SequenceModification service = new AddModification();
        editService.modifySequence(request, service).getSequenceAsString();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(path = CUT_SEQ_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cutSequence(@RequestBody ModificationRequest request) throws GenBankFileNotFound, GenBankFileEditorException, UnknownSequenceType, SequenceValidationException, RequestBodyValidationException {
        SequenceModification service = new CutModification();
        editService.modifySequence(request, service).getSequenceAsString();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(path = MODIFY_SEQ_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> modifySequence(@RequestBody ModificationRequest request) throws GenBankFileNotFound, GenBankFileEditorException, UnknownSequenceType, SequenceValidationException, RequestBodyValidationException {
        SequenceModification service = new ModifyModification();
        editService.modifySequence(request, service).getSequenceAsString();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
