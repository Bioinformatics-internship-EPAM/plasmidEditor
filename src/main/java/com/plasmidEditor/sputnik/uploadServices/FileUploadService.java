package com.plasmidEditor.sputnik.uploadServices;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService<T> {
	public void upload(MultipartFile file);
}
