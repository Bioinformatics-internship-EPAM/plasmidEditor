package com.plasmidEditor.sputnik.uploadServices;

import java.io.InputStream;

import com.plasmidEditor.sputnik.exceptions.FileUploadException;

public interface FileUploadService<T> {
	public void upload(InputStream inputStream) throws FileUploadException;
}
