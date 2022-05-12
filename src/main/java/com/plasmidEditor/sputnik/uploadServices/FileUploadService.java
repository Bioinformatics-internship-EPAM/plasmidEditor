package com.plasmidEditor.sputnik.uploadServices;

import java.io.InputStream;

public interface FileUploadService<T> {
	public void upload(InputStream file);
}
