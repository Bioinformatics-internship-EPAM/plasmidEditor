package com.plasmideditor.rocket.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plasmideditor.rocket.web.domains.request.ModificationRequest;
import com.plasmideditor.rocket.web.exceptions.GenBankFileEditorException;
import com.plasmideditor.rocket.web.exceptions.GenBankFileNotFoundException;
import com.plasmideditor.rocket.web.exceptions.SequenceValidationException;
import com.plasmideditor.rocket.web.exceptions.UnknownSequenceTypeException;
import com.plasmideditor.rocket.web.service.EditService;
import org.biojava.nbio.core.sequence.DNASequence;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.plasmideditor.rocket.web.configuration.ApiConstants.*;
import static com.plasmideditor.rocket.web.service.utils.Operations.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileEditorController.class)
public class FileEditorControllerTest {
    private final static String testSequence = "AAAAA";
    private final static String sequenceForModification = "ATGAAAAAC";

    private final String FILE_DOES_NOT_EXISTS = "Requested file does not exist in database";
    private final String UNKNOWN_SEQ_TYPE = "Sequence type is unknown";
    private final String NOT_SUITABLE_SEQ = "Sequence not suitable for file";
    private final String INTERNAL_ERR = "Internal modification error";

    private final static String DNA_FILE_ID = "1";
    private final static String FILE_VERSION = "v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EditService editService;

    private static ModificationRequest request;
    private static String json;

    @BeforeAll
    public static void init() throws JsonProcessingException {
        json = createRequest(testSequence);
        request = new ModificationRequest(3, testSequence, DNA_FILE_ID, FILE_VERSION);
    }

    @Test
    public void testSuccessfulResponseDuringAddOperation() throws Exception {
        when(editService.modifySequence(request, ADD))
                .thenReturn(new DNASequence(sequenceForModification));

        checkResponseIsOk(ADD_SEQ_PATH, json);
    }

    @Test
    public void testSuccessfulResponseDuringCutOperation() throws Exception {
        when(editService.modifySequence(request, CUT))
                .thenReturn(new DNASequence(sequenceForModification));

        checkResponseIsOk(CUT_SEQ_PATH, json);
    }

    @Test
    public void testSuccessfulResponseDuringModifyOperation() throws Exception {
        when(editService.modifySequence(request, MODIFY))
                .thenReturn(new DNASequence(sequenceForModification));

        checkResponseIsOk(MODIFY_SEQ_PATH, json);
    }

    @Test
    public void testFileNotFoundInDatabaseDuringAddOperation() throws Exception {
        when(editService.modifySequence(request, ADD))
                .thenThrow(GenBankFileNotFoundException.class);

        checkResponseIsBadRequest(ADD_SEQ_PATH, json, FILE_DOES_NOT_EXISTS);
    }

    @Test
    public void testFileNotFoundInDatabaseDuringCutOperation() throws Exception {
        when(editService.modifySequence(request, CUT)).thenThrow(GenBankFileNotFoundException.class);

        checkResponseIsBadRequest(CUT_SEQ_PATH, json, FILE_DOES_NOT_EXISTS);
    }

    @Test
    public void testFileNotFoundInDatabaseDuringModifyOperation() throws Exception {
        when(editService.modifySequence(request, MODIFY))
                .thenThrow(GenBankFileNotFoundException.class);

        checkResponseIsBadRequest(MODIFY_SEQ_PATH, json, FILE_DOES_NOT_EXISTS);
    }

    @Test
    public void testUnknownSequenceTypeDuringAddOperation() throws Exception {
        when(editService.modifySequence(request, ADD))
                .thenThrow(UnknownSequenceTypeException.class);

        checkResponseIsBadRequest(ADD_SEQ_PATH, json, UNKNOWN_SEQ_TYPE);
    }

    @Test
    public void testUnknownSequenceTypeDuringCutOperation() throws Exception {
        when(editService.modifySequence(request, CUT))
                .thenThrow(UnknownSequenceTypeException.class);

        checkResponseIsBadRequest(CUT_SEQ_PATH, json, UNKNOWN_SEQ_TYPE);
    }

    @Test
    public void testUnknownSequenceTypeDuringModifyOperation() throws Exception {
        when(editService.modifySequence(request, MODIFY))
                .thenThrow(UnknownSequenceTypeException.class);

        checkResponseIsBadRequest(MODIFY_SEQ_PATH, json, UNKNOWN_SEQ_TYPE);
    }

    @Test
    public void testSequenceValidationErrorDuringAddOperation() throws Exception {
        String testWrongSequence = "Wrong";
        String json = createRequest(testWrongSequence);
        ModificationRequest request = new ModificationRequest(3, testWrongSequence, DNA_FILE_ID, FILE_VERSION);

        doThrow(SequenceValidationException.class).when(editService).modifySequence(request, ADD);

        checkResponseIsUnprocessableEntity(ADD_SEQ_PATH, json, NOT_SUITABLE_SEQ);
    }

    @Test
    public void testSequenceValidationErrorDuringModifyOperation() throws Exception {
        String testWrongSequence = "Wrong";
        String json = createRequest(testWrongSequence);
        ModificationRequest request = new ModificationRequest(3, testWrongSequence, DNA_FILE_ID, FILE_VERSION);
        doThrow(SequenceValidationException.class).when(editService).modifySequence(request, MODIFY);

        checkResponseIsUnprocessableEntity(MODIFY_SEQ_PATH, json, NOT_SUITABLE_SEQ);
    }

    @Test
    public void testErrorWithGenBankFileDuringAddOperation() throws Exception {
        when(editService.modifySequence(request, ADD))
                .thenThrow(GenBankFileEditorException.class);

        checkResponseIsInternalServerError(ADD_SEQ_PATH, json, INTERNAL_ERR);
    }

    @Test
    public void testErrorWithGenBankFileDuringCutOperation() throws Exception {
        when(editService.modifySequence(request, CUT))
                .thenThrow(GenBankFileEditorException.class);

        checkResponseIsInternalServerError(CUT_SEQ_PATH, json, INTERNAL_ERR);
    }

    @Test
    public void testErrorWithGenBankFileDuringModifyOperation() throws Exception {
        when(editService.modifySequence(request, MODIFY))
                .thenThrow(GenBankFileEditorException.class);

        checkResponseIsInternalServerError(MODIFY_SEQ_PATH, json, INTERNAL_ERR);
    }

    private static String createRequest(String testSequence) throws JsonProcessingException {
        ModificationRequest request = new ModificationRequest(3, testSequence, DNA_FILE_ID, FILE_VERSION);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(request);
    }

    private void checkResponseIsOk(String path, String json) throws Exception {
        this.mockMvc.perform(post(EDIT_FILE_PATH + path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    private void checkResponseIsBadRequest(String path, String json, String content) throws Exception {
        this.mockMvc.perform(post(EDIT_FILE_PATH + path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(content)));
    }

    private void checkResponseIsUnprocessableEntity(String path, String json, String content) throws Exception {
        this.mockMvc.perform(post(EDIT_FILE_PATH + path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
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
