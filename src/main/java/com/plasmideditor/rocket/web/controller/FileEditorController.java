package com.plasmideditor.rocket.web.controller;

import com.plasmideditor.rocket.web.domains.request.ModificationRequest;
import com.plasmideditor.rocket.web.service.EditService;
import com.plasmideditor.rocket.web.service.modifications.AddModification;
import com.plasmideditor.rocket.web.service.modifications.CutModification;
import com.plasmideditor.rocket.web.service.modifications.ModifyModification;
import com.plasmideditor.rocket.web.service.modifications.SequenceModification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.plasmideditor.rocket.web.configuration.ApiConstants.*;

@Controller
@RequestMapping(value = EDIT_FILE_PATH)
@ResponseStatus(HttpStatus.CREATED)
public class FileEditorController {

    @Autowired
    private EditService editService;

    @PostMapping(path = ADD_SEQ_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addSequenceToFile(@RequestBody ModificationRequest request) {
        SequenceModification service = new AddModification();
        editService.modifySequence(request, service).getSequenceAsString();
    }

    @PostMapping(path = CUT_SEQ_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void cutSequence(@RequestBody ModificationRequest request) {
        SequenceModification service = new CutModification();
        editService.modifySequence(request, service).getSequenceAsString();
    }

    @PostMapping(path = MODIFY_SEQ_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void modifySequence(@RequestBody ModificationRequest request) {
        SequenceModification service = new ModifyModification();
        editService.modifySequence(request, service).getSequenceAsString();
    }
}
