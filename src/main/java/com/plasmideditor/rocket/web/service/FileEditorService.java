package com.plasmideditor.rocket.web.service;

import com.plasmideditor.rocket.web.service.exceptions.FileEditorUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileEditorService<T> {
    void uploadFile(MultipartFile file) throws FileEditorUploadException;
}