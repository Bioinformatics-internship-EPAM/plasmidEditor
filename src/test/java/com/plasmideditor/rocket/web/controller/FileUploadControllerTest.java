package com.plasmideditor.rocket.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plasmideditor.rocket.web.exceptions.FileAlreadyExistsException;
import com.plasmideditor.rocket.web.exceptions.FileUploadException;
import com.plasmideditor.rocket.web.exceptions.SequenceValidationException;
import com.plasmideditor.rocket.web.response.ErrorResponse;
import com.plasmideditor.rocket.web.service.DNAFileUploadService;
import com.plasmideditor.rocket.web.service.ProteinFileUploadService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.plasmideditor.rocket.web.configuration.ApiConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileUploadController.class)

public class FileUploadControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @MockBean
    private transient DNAFileUploadService dnaFileEditorService;
    @MockBean
    private transient ProteinFileUploadService proteinFileEditorService;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private static MockMultipartFile multipartFile;

    @BeforeAll
    public static void init() throws IOException {
        multipartFile = getMultipartFileFromString("Gen bank file example");
    }


    @Test
    public void testSuccessfullyUploadDNAFile() throws Exception {
        mockMvc.perform(
                        multipart(ROOT_ENDPOINT + DNA_ENDPOINT)
                                .file(multipartFile))
                .andExpect(status().isCreated());
    }

    @Test
    public void testSuccessfullyUploadProteinFile() throws Exception {
        mockMvc.perform(
                        multipart(ROOT_ENDPOINT + PROTEIN_ENDPOINT)
                                .file(multipartFile))
                .andExpect(status().isCreated());
    }

    @Test
    public void testFiledToUploadFileEditorUploadException() throws Exception {
        String fileEditorUploadExceptionMsg = "Failed to upload GenBank file";
        mockServicesWithException(new FileUploadException(fileEditorUploadExceptionMsg));

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                RocketExceptionHandler.FILE_EDITOR_UPLOAD_EXCEPTION_MSG + fileEditorUploadExceptionMsg);
        String expectedResponseBody = objectMapper.writeValueAsString(expectedErrorResponse);

        assertResponseErrorMessage(DNA_ENDPOINT, expectedResponseBody, 400);
        assertResponseErrorMessage(PROTEIN_ENDPOINT, expectedResponseBody, 400);
    }

    @Test
    public void testFiledToUploadSequenceValidationException() throws Exception {
        String sequenceValidationExceptionMsg = "Sequence is invalid";
        mockServicesWithException(new SequenceValidationException(sequenceValidationExceptionMsg));

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                RocketExceptionHandler.SEQUENCE_VALIDATION_EXCEPTION_MSG + sequenceValidationExceptionMsg);
        String expectedResponseBody = objectMapper.writeValueAsString(expectedErrorResponse);

        assertResponseErrorMessage(DNA_ENDPOINT, expectedResponseBody, 422);
        assertResponseErrorMessage(PROTEIN_ENDPOINT, expectedResponseBody, 422);
    }

    @Test
    public void testFiledToUploadGenBankFileAlreadyExistsException() throws Exception {
        String genBankFileAlreadyExistsExceptionMsg = "Sequence is invalid";
        mockServicesWithException(new FileAlreadyExistsException(genBankFileAlreadyExistsExceptionMsg));

        ErrorResponse expectedErrorResponse = new ErrorResponse(genBankFileAlreadyExistsExceptionMsg);
        String expectedResponseBody = objectMapper.writeValueAsString(expectedErrorResponse);

        assertResponseErrorMessage(DNA_ENDPOINT, expectedResponseBody, 400);
        assertResponseErrorMessage(PROTEIN_ENDPOINT, expectedResponseBody, 400);
    }

    private static MockMultipartFile getMultipartFileFromString(String string) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(string.getBytes());
        return new MockMultipartFile(
                "file",
                "filename",
                "text/plain",
                inputStream.readAllBytes()
        );
    }

    private void mockServicesWithException(Exception exception) throws FileUploadException, SequenceValidationException, FileAlreadyExistsException {
        doThrow(exception).when(dnaFileEditorService).uploadFile(any());
        doThrow(exception).when(proteinFileEditorService).uploadFile(any());
    }

    private void assertResponseErrorMessage(String path, String expectedErrorResponse, int expectedStatus) throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        multipart(ROOT_ENDPOINT + path)
                                .file(multipartFile))
                .andExpect(selectMatcherForResponseStatus(expectedStatus))
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedErrorResponse, actualResponseBody);
    }

    private ResultMatcher selectMatcherForResponseStatus(int responseStatus) {
        switch (responseStatus) {
            case 400:
                return status().isBadRequest();
            case 422:
                return status().isUnprocessableEntity();
            default:
                throw new IllegalArgumentException();
        }
    }
}
