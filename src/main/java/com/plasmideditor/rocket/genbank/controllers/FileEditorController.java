package com.plasmideditor.rocket.genbank.controllers;

import com.plasmideditor.rocket.genbank.domains.request.FileRequest;
import com.plasmideditor.rocket.genbank.domains.request.ModificationRequest;
import com.plasmideditor.rocket.genbank.services.DNAFileEditorServiceImpl;
import com.plasmideditor.rocket.genbank.services.EditService;
import com.plasmideditor.rocket.genbank.services.exceptions.GenBankFileEditorException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.plasmideditor.rocket.genbank.configuration.ApiConstants.ADD_SEQ_PATH;
import static com.plasmideditor.rocket.genbank.configuration.ApiConstants.CUT_SEQ_PATH;
import static com.plasmideditor.rocket.genbank.configuration.ApiConstants.EDIT_FILE_PATH;

@Controller
@RequestMapping(value = EDIT_FILE_PATH)
public class FileEditorController {

    private final DNAFileEditorServiceImpl fileEditorService;

    public FileEditorController(DNAFileEditorServiceImpl fileEditorService) {
        this.fileEditorService = fileEditorService;
    }

    @PostMapping(path = ADD_SEQ_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addSequenceToFile(@RequestBody ModificationRequest request) {
        FileRequest fileRequest = request.getFileRequest();
        // download from db
        // check with instanceof ?




        return ResponseEntity.ok("");
    }

    @PostMapping(path = CUT_SEQ_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cutSequence(@RequestBody ModificationRequest request) throws GenBankFileEditorException {
//        editor.cutGenBankFile(request.getSequenceInfoRequest().getStartPosition(),
//                request.getSequenceInfoRequest().getSequence(),
//                request.getFileRequest().getFileId(),
//                request.getFileRequest().getFileVersion());

        return ResponseEntity.ok("");
    }
}
