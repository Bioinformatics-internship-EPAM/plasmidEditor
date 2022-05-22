package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.web.exceptions.FileUploadException;
import com.plasmideditor.rocket.web.exceptions.FileAlreadyExistsException;
import com.plasmideditor.rocket.web.exceptions.SequenceValidationException;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public interface FileUploadService<T> {
    void uploadFile(InputStream file) throws FileUploadException, SequenceValidationException, FileAlreadyExistsException;
}