package com.plasmidEditor.sputnik.controllers;

import com.plasmidEditor.sputnik.editor.EditorParameters;
import com.plasmidEditor.sputnik.editor.GenBankEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.plasmidEditor.sputnik.utils.RequestEditorPath.*;

@Controller
@RequestMapping(path = EDIT_PATH)
@ResponseStatus(HttpStatus.CREATED)
public class GenBankEditorController {
    @Autowired
    private transient GenBankEditor editor;

    @PostMapping(path = ADD_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addSequence(@RequestBody EditorParameters request) {
        return editor.add(request).getSequenceAsString();
    }

    @PostMapping(path = MODIFY_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String modifySequence(@RequestBody EditorParameters request) {
        return editor.modify(request).getSequenceAsString();
    }

    @PostMapping(path = CUT_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String cutSequence(@RequestBody EditorParameters request) {
        return editor.cut(request).getSequenceAsString();
    }
}
