package com.plasmideditor.rocket.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plasmideditor.rocket.web.domains.request.ModificationRequest;
import com.plasmideditor.rocket.web.service.EditService;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileEditorException;
import com.plasmideditor.rocket.web.service.exceptions.GenBankFileNotFound;
import com.plasmideditor.rocket.web.service.exceptions.SequenceValidationException;
import com.plasmideditor.rocket.web.service.exceptions.UnknownSequenceType;
import com.plasmideditor.rocket.web.service.modifications.AddModification;
import com.plasmideditor.rocket.web.service.modifications.CutModification;
import com.plasmideditor.rocket.web.service.modifications.ModifyModification;
import org.biojava.nbio.core.sequence.DNASequence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.plasmideditor.rocket.web.configuration.ApiConstants.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileEditorController.class)
public class FileEditorControllerTest {
    private final String testSequence = "AAAAA";
    private final String sequenceForModification = "ATGAAAAAC";

    private final String FILE_DOES_NOT_EXISTS = "Requested file does not exist in database";
    private final String UNKNOWN_SEQ_TYPE = "Sequence type is unknown";
    private final String NOT_SUITABLE_SEQ = "Sequence not suitable for file";
    private final String INTERNAL_ERR = "Internal modification error";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EditService editService;

    @Test
    public void testSuccessfulResponseDuringAddOperation() throws Exception {
        String json = createRequest(testSequence);
        ModificationRequest request = new ModificationRequest(3, testSequence, "1", "v1");
        when(editService.modifySequence(eq(request), any(AddModification.class)))
                .thenReturn(new DNASequence(sequenceForModification));

        checkResponseIsOk(ADD_SEQ_PATH, json);
    }

    @Test
    public void testSuccessfulResponseDuringCutOperation() throws Exception {
        String json = createRequest(testSequence);
        ModificationRequest request = new ModificationRequest(3, testSequence, "1", "v1");
        when(editService.modifySequence(eq(request), any(CutModification.class)))
                .thenReturn(new DNASequence(sequenceForModification));

        checkResponseIsOk(CUT_SEQ_PATH, json);
    }

    @Test
    public void testSuccessfulResponseDuringModifyOperation() throws Exception {
        String json = createRequest(testSequence);
        ModificationRequest request = new ModificationRequest(3, testSequence, "1", "v1");
        when(editService.modifySequence(eq(request), any(ModifyModification.class)))
                .thenReturn(new DNASequence(sequenceForModification));

        checkResponseIsOk(MODIFY_SEQ_PATH, json);
    }

    @Test
    public void testFileNotFoundInDatabaseDuringAddOperation() throws Exception {
        String json = createRequest(testSequence);
        ModificationRequest request = new ModificationRequest(3, testSequence, "1", "v1");
        when(editService.modifySequence(eq(request), any(AddModification.class)))
                .thenThrow(GenBankFileNotFound.class);

        checkResponseIsBadRequest(ADD_SEQ_PATH, json, FILE_DOES_NOT_EXISTS);
    }

    @Test
    public void testFileNotFoundInDatabaseDuringCutOperation() throws Exception {
        String json = createRequest(testSequence);
        ModificationRequest request = new ModificationRequest(3, testSequence, "1", "v1");
        when(editService.modifySequence(eq(request), any(CutModification.class))).thenThrow(GenBankFileNotFound.class);

        checkResponseIsBadRequest(CUT_SEQ_PATH, json, FILE_DOES_NOT_EXISTS);
    }

    @Test
    public void testFileNotFoundInDatabaseDuringModifyOperation() throws Exception {
        String json = createRequest(testSequence);
        ModificationRequest request = new ModificationRequest(3, testSequence, "1", "v1");
        when(editService.modifySequence(eq(request), any(ModifyModification.class)))
                .thenThrow(GenBankFileNotFound.class);

        checkResponseIsBadRequest(MODIFY_SEQ_PATH, json, FILE_DOES_NOT_EXISTS);
    }

    @Test
    public void testUnknownSequenceTypeDuringAddOperation() throws Exception {
        String json = createRequest(testSequence);
        ModificationRequest request = new ModificationRequest(3, testSequence, "1", "v1");
        when(editService.modifySequence(eq(request), any(AddModification.class)))
                .thenThrow(UnknownSequenceType.class);

        checkResponseIsBadRequest(ADD_SEQ_PATH, json, UNKNOWN_SEQ_TYPE);
    }

    @Test
    public void testUnknownSequenceTypeDuringCutOperation() throws Exception {
        String json = createRequest(testSequence);
        ModificationRequest request = new ModificationRequest(3, testSequence, "1", "v1");
        when(editService.modifySequence(eq(request), any(CutModification.class)))
                .thenThrow(UnknownSequenceType.class);

        checkResponseIsBadRequest(CUT_SEQ_PATH, json, UNKNOWN_SEQ_TYPE);
    }

    @Test
    public void testUnknownSequenceTypeDuringModifyOperation() throws Exception {
        String json = createRequest(testSequence);
        ModificationRequest request = new ModificationRequest(3, testSequence, "1", "v1");
        when(editService.modifySequence(eq(request), any(ModifyModification.class)))
                .thenThrow(UnknownSequenceType.class);

        checkResponseIsBadRequest(MODIFY_SEQ_PATH, json, UNKNOWN_SEQ_TYPE);
    }

    @Test
    public void testSequenceValidationErrorDuringAddOperation() throws Exception {
        String testWrongSequence = "Wrong";
        String json = createRequest(testWrongSequence);
        ModificationRequest request = new ModificationRequest(3, testWrongSequence, "1", "v1");

        doThrow(SequenceValidationException.class).when(editService).modifySequence(eq(request), any(AddModification.class));

        checkResponseIsBadRequest(ADD_SEQ_PATH, json, NOT_SUITABLE_SEQ);
    }

    @Test
    public void testSequenceValidationErrorDuringModifyOperation() throws Exception {
        String testWrongSequence = "Wrong";
        String json = createRequest(testWrongSequence);
        ModificationRequest request = new ModificationRequest(3, testWrongSequence, "1", "v1");
        doThrow(SequenceValidationException.class).when(editService).modifySequence(eq(request), any(ModifyModification.class));

        checkResponseIsBadRequest(MODIFY_SEQ_PATH, json, NOT_SUITABLE_SEQ);
    }

    @Test
    public void testErrorWithGenBankFileDuringAddOperation() throws Exception {
        String json = createRequest(testSequence);
        ModificationRequest request = new ModificationRequest(3, testSequence, "1", "v1");
        when(editService.modifySequence(eq(request), any(AddModification.class)))
                .thenThrow(GenBankFileEditorException.class);

        checkResponseIsInternalServerError(ADD_SEQ_PATH, json, INTERNAL_ERR);
    }

    @Test
    public void testErrorWithGenBankFileDuringCutOperation() throws Exception {
        String json = createRequest(testSequence);
        ModificationRequest request = new ModificationRequest(3, testSequence, "1", "v1");
        when(editService.modifySequence(eq(request), any(CutModification.class)))
                .thenThrow(GenBankFileEditorException.class);

        checkResponseIsInternalServerError(CUT_SEQ_PATH, json, INTERNAL_ERR);
    }

    @Test
    public void testErrorWithGenBankFileDuringModifyOperation() throws Exception {
        String json = createRequest(testSequence);
        ModificationRequest request = new ModificationRequest(3, testSequence, "1", "v1");
        when(editService.modifySequence(eq(request), any(ModifyModification.class)))
                .thenThrow(GenBankFileEditorException.class);

        checkResponseIsInternalServerError(MODIFY_SEQ_PATH, json, INTERNAL_ERR);
    }

    private String createRequest(String testSequence) throws JsonProcessingException {
        ModificationRequest request = new ModificationRequest(3, testSequence, "1", "v1");
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
