package com.plasmidEditor.sputnik.controllers;

import com.plasmidEditor.sputnik.editor.EditorParameters;
import com.plasmidEditor.sputnik.editor.GenBankEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.plasmidEditor.sputnik.utils.RequestEditorPath.*;

@Controller
@RequestMapping(path = EDIT_PATH)
public class GenBankEditorController {
    @Autowired
    private GenBankEditor editor;
    @PostMapping(path = ADD_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addSequence(@RequestBody EditorParameters request) {
        return editor.add(request.getSubsequence(), request.getPosition(), request.getFileContent()).getSequenceAsString();
    }

    @PostMapping(path = MODIFY_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String modifySequence(@RequestBody EditorParameters request) {
        return editor.modify(request.getSubsequence(), request.getPosition(), request.getFileContent()).getSequenceAsString();
    }

    @PostMapping(path = CUT_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String cutSequence(@RequestBody EditorParameters request) {
        return editor.cut(request.getPosition(), request.getCutSize(), request.getFileContent()).getSequenceAsString();
    }
}
