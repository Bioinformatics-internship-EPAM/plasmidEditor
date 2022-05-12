package com.plasmidEditor.sputnik.controllers;

import java.io.InputStream;
import lombok.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plasmidEditor.sputnik.uploadServices.*;

@RestController
@RequestMapping(value = "/genbank")
public class FileUploadController {
	private final DNAFileUploadService dnaFileUploadService;
	private final ProteinFileUploadService proteinFileUploadService;
	
	@Data
	@AllArgsConstructor
	private static class JsonError {
		private String error;
		private String message;
	}
	
	@Autowired
	public FileUploadController(DNAFileUploadService dnaFileUploadService, 
			ProteinFileUploadService proteinFileUploadService) {
		this.dnaFileUploadService = dnaFileUploadService;
		this.proteinFileUploadService = proteinFileUploadService;
	}
	
	@PostMapping(path="/genbank/dna", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JsonError> uploadDNAFile(@RequestParam("file") InputStream file) {
		dnaFileUploadService.upload(file);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping(path="/genbank/protein", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JsonError> uploadProteinFile(@RequestParam("file") InputStream file) {
		proteinFileUploadService.upload(file);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<JsonError> handleException(Exception e) {
		return ResponseEntity.badRequest().body(new JsonError("failed to upload genbank file", e.getMessage()));
	}
}
