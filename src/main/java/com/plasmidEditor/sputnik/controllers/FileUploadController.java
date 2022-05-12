package com.plasmidEditor.sputnik.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plasmidEditor.sputnik.uploadServices.*;

@RestController
public class FileUploadController {
	private final DNAFileUploadService dnaFileUploadService;
	private final ProteinFileUploadService proteinFileUploadService;
	
	@Autowired
	public FileUploadController(DNAFileUploadService dnaFileUploadService, 
			ProteinFileUploadService proteinFileUploadService) {
		this.dnaFileUploadService = dnaFileUploadService;
		this.proteinFileUploadService = proteinFileUploadService;
	}
	
	@PostMapping(path="/genbank/dna", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> uploadDNAFile(@RequestParam("file") MultipartFile file) {
		dnaFileUploadService.upload(file);
		return ResponseEntity.ok("File was successfully uploaded");
	}
	
	@PostMapping(path="/genbank/protein", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> uploadProteinFile(@RequestParam("file") MultipartFile file) {
		proteinFileUploadService.upload(file);
		return ResponseEntity.ok("File was successfully uploaded");
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e) {
		return ResponseEntity.badRequest().build();
	}
}
