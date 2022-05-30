package com.plasmidEditor.sputnik.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plasmidEditor.sputnik.exceptions.FileUploadException;
import com.plasmidEditor.sputnik.uploadServices.*;
import com.plasmidEditor.sputnik.utils.UploadPathsConstants;

@RestController
@RequestMapping(value = UploadPathsConstants.ROOT_UPLOAD_PATH)
@ResponseStatus(HttpStatus.CREATED)
public class FileUploadController {
	private transient final DNAFileUploadService dnaFileUploadService;
	private transient final ProteinFileUploadService proteinFileUploadService;
	
	
	@Autowired
	public FileUploadController(DNAFileUploadService dnaFileUploadService, 
			ProteinFileUploadService proteinFileUploadService) {
		this.dnaFileUploadService = dnaFileUploadService;
		this.proteinFileUploadService = proteinFileUploadService;
	}
	
	@PostMapping(path=UploadPathsConstants.DNA_UPLOAD_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
	public void uploadDNAFile(@RequestBody MultipartFile file) throws FileUploadException {
		try {
			dnaFileUploadService.upload(file.getInputStream());
		} catch(IOException e) {
			throw new FileUploadException(e.getMessage(), e);
		}
	}
	
	@PostMapping(path=UploadPathsConstants.PROTEIN_UPLOAD_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
	public void uploadProteinFile(@RequestBody MultipartFile file) throws FileUploadException {
		try {
			proteinFileUploadService.upload(file.getInputStream());
		} catch(IOException e) {
			throw new FileUploadException(e.getMessage(), e);
		}
	}
}
