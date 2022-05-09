package com.plasmideditor.rocket.web.service;

import java.io.InputStream;

public interface FileUploadService<T> {
    void uploadFile(InputStream file);
}