package com.plasmideditor.rocket.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plasmideditor.rocket.web.domains.request.FileRequest;
import com.plasmideditor.rocket.web.domains.request.ModificationRequest;
import com.plasmideditor.rocket.web.domains.request.SequenceInfoRequest;
import com.plasmideditor.rocket.web.service.EditService;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileEditorException;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileNotFound;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import com.plasmideditor.rocket.web.service.exceptions.UnknownSequenceType;
import org.biojava.nbio.core.sequence.DNASequence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.plasmideditor.rocket.web.configuration.ApiConstants.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileEditorController.class)
public class FileEditorControllerTest {
    private final String testSequence = "AAAAA";
    private final String sequenceForModification = "ATGAAAAAC";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EditService editService;

    @Test
    public void testSuccessfulResponseDuringAddOperation() throws Exception {
        String json = createRequest(testSequence);
        when(editService.addGenBankFile(3, testSequence, "1", "v1"))
                .thenReturn(new DNASequence(sequenceForModification));

        checkResponseIsOk(ADD_SEQ_PATH, json);
    }

    @Test
    public void testSuccessfulResponseDuringCutOperation() throws Exception {
        String json = createRequest(testSequence);
        when(editService.cutGenBankFile(3, testSequence, "1", "v1"))
                .thenReturn(new DNASequence(sequenceForModification));

        checkResponseIsOk(CUT_SEQ_PATH, json);
    }

    @Test
    public void testSuccessfulResponseDuringModifyOperation() throws Exception {
        String json = createRequest(testSequence);
        when(editService.modifyGenBankFile(3, testSequence, "1", "v1"))
                .thenReturn(new DNASequence(sequenceForModification));

        checkResponseIsOk(MODIFY_SEQ_PATH, json);
    }

    @Test
    public void testFileNotFoundInDatabaseDuringAddOperation() throws Exception {
        String json = createRequest(testSequence);
        when(editService.addGenBankFile(3, testSequence, "1", "v1"))
                .thenThrow(GenBankFileNotFound.class);

        checkResponseIsBadRequest(ADD_SEQ_PATH, json, "Requested file does not exist in database");
    }

    @Test
    public void testFileNotFoundInDatabaseDuringCutOperation() throws Exception {
        String json = createRequest(testSequence);

        when(editService.cutGenBankFile(3, testSequence, "1", "v1")).thenThrow(GenBankFileNotFound.class);

        checkResponseIsBadRequest(CUT_SEQ_PATH, json, "Requested file does not exist in database");
    }

    @Test
    public void testFileNotFoundInDatabaseDuringModifyOperation() throws Exception {
        String json = createRequest(testSequence);
        when(editService.modifyGenBankFile(3, testSequence, "1", "v1")).thenThrow(GenBankFileNotFound.class);

        checkResponseIsBadRequest(MODIFY_SEQ_PATH, json, "Requested file does not exist in database");
    }

    @Test
    public void testUnknownSequenceTypeDuringAddOperation() throws Exception {
        String json = createRequest(testSequence);
        when(editService.addGenBankFile(3, testSequence, "1", "v1"))
                .thenThrow(UnknownSequenceType.class);

        checkResponseIsBadRequest(ADD_SEQ_PATH, json, "Sequence type is unknown");
    }

    @Test
    public void testUnknownSequenceTypeDuringCutOperation() throws Exception {
        String json = createRequest(testSequence);
        when(editService.cutGenBankFile(3, testSequence, "1", "v1"))
                .thenThrow(UnknownSequenceType.class);

        checkResponseIsBadRequest(CUT_SEQ_PATH, json, "Sequence type is unknown");
    }

    @Test
    public void testUnknownSequenceTypeDuringModifyOperation() throws Exception {
        String json = createRequest(testSequence);
        when(editService.modifyGenBankFile(3, testSequence, "1", "v1"))
                .thenThrow(UnknownSequenceType.class);

        checkResponseIsBadRequest(MODIFY_SEQ_PATH, json, "Sequence type is unknown");
    }

    @Test
    public void testSequenceValidationErrorDuringAddOperation() throws Exception {
        String testWrongSequence = "Wrong";
        String json = createRequest(testWrongSequence);
        doThrow(SequenceValidationException.class).when(editService).addGenBankFile(3, testWrongSequence, "1", "v1");

        checkResponseIsBadRequest(ADD_SEQ_PATH, json, "Sequence not suitable for file: expected type is ");
    }

    @Test
    public void testSequenceValidationErrorDuringModifyOperation() throws Exception {
        String testWrongSequence = "Wrong";
        String json = createRequest(testWrongSequence);
        doThrow(SequenceValidationException.class).when(editService).modifyGenBankFile(3, testWrongSequence, "1", "v1");

        checkResponseIsBadRequest(MODIFY_SEQ_PATH, json,"Sequence not suitable for file: expected type is ");
    }

    @Test
    public void testErrorWithGenBankFileDuringAddOperation() throws Exception {
        String json = createRequest(testSequence);
        when(editService.addGenBankFile(3, testSequence, "1", "v1"))
                .thenThrow(GenBankFileEditorException.class);

        checkResponseIsInternalServerError(ADD_SEQ_PATH, json, "Internal modification error");
    }

    @Test
    public void testErrorWithGenBankFileDuringCutOperation() throws Exception {
        String json = createRequest(testSequence);
        when(editService.cutGenBankFile(3, testSequence, "1", "v1"))
                .thenThrow(GenBankFileEditorException.class);

        checkResponseIsInternalServerError(CUT_SEQ_PATH, json, "Internal modification error");
    }

    @Test
    public void testErrorWithGenBankFileDuringModifyOperation() throws Exception {
        String json = createRequest(testSequence);
        when(editService.modifyGenBankFile(3, testSequence, "1", "v1"))
                .thenThrow(GenBankFileEditorException.class);

        checkResponseIsInternalServerError(MODIFY_SEQ_PATH, json, "Internal modification error");
    }

    private String createRequest(String testSequence) throws JsonProcessingException {
        FileRequest fileRequest = new FileRequest("1", "v1");
        SequenceInfoRequest sequenceInfoRequest = new SequenceInfoRequest(3, testSequence);
        ModificationRequest request = new ModificationRequest(sequenceInfoRequest, fileRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(request);
    }

    private void checkResponseIsOk(String path, String json) throws Exception {
        this.mockMvc.perform(post(EDIT_FILE_PATH + path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(sequenceForModification));
    }

    private void checkResponseIsBadRequest(String path, String json, String content) throws Exception {
        this.mockMvc.perform(post(EDIT_FILE_PATH + path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(content)));
    }

    private void checkResponseIsInternalServerError(String path, String json, String content) throws Exception {
        this.mockMvc.perform(post(EDIT_FILE_PATH + path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(content)));
    }

}
