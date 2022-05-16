package com.plasmidEditor.sputnik.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plasmidEditor.sputnik.uploadServices.*;

@RestController
@RequestMapping(value = "/genbank")
@ResponseStatus(HttpStatus.CREATED)
public class FileUploadController {
	private final DNAFileUploadService dnaFileUploadService;
	private final ProteinFileUploadService proteinFileUploadService;
	
	
	@Autowired
	public FileUploadController(DNAFileUploadService dnaFileUploadService, 
			ProteinFileUploadService proteinFileUploadService) {
		this.dnaFileUploadService = dnaFileUploadService;
		this.proteinFileUploadService = proteinFileUploadService;
	}
	
	@PostMapping(path="/dna", produces=MediaType.APPLICATION_JSON_VALUE)
	public void uploadDNAFile(@RequestParam("file") MultipartFile file) throws FileUploadException, IOException {
		dnaFileUploadService.upload(file.getInputStream());
	}
	
	@PostMapping(path="/protein", produces=MediaType.APPLICATION_JSON_VALUE)
	public void uploadProteinFile(@RequestParam("file") MultipartFile file) throws FileUploadException, IOException {
		proteinFileUploadService.upload(file.getInputStream());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<JsonError> handleException(Exception e) {
		return ResponseEntity.badRequest().body(new ErrorResponseBody("failed to upload genbank file", e.getMessage()));
	}
}
