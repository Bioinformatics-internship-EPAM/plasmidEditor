package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.web.exceptions.FileEditorUploadException;
import com.plasmideditor.rocket.web.exceptions.GenBankFileAlreadyExistsException;
import com.plasmideditor.rocket.web.exceptions.SequenceValidationException;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public interface FileEditorService<T> {
    void uploadFile(InputStream file) throws FileEditorUploadException, SequenceValidationException, GenBankFileAlreadyExistsException;
}