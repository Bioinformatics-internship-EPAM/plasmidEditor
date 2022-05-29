package com.plasmidEditor.sputnik.controllers;

import com.plasmidEditor.sputnik.editor.EditorParameters;
import com.plasmidEditor.sputnik.editor.GenBankEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.plasmidEditor.sputnik.utils.RequestEditorPath.*;

@Controller
@RequestMapping(path = EDIT_PATH)
@ResponseStatus(HttpStatus.CREATED)
public class GenBankEditorController {
    @Autowired
    private transient GenBankEditor editor;
    @PostMapping(path = ADD_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addSequence(@RequestBody EditorParameters request) {
        editor.add(request.getSubsequence(), request.getPosition(), request.getFileContent());
    }

    @PostMapping(path = MODIFY_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void modifySequence(@RequestBody EditorParameters request) {
        editor.modify(request.getSubsequence(), request.getPosition(), request.getFileContent());
    }

    @PostMapping(path = CUT_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void cutSequence(@RequestBody EditorParameters request) {
        editor.cut(request.getPosition(), request.getCutSize(), request.getFileContent());
    }
}
