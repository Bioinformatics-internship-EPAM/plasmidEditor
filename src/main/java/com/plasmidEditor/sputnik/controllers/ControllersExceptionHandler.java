package com.plasmidEditor.sputnik.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllersExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<JsonError> handleException(Exception e) {
		return ResponseEntity.badRequest().body(new JsonError("invalid file", e.getMessage()));
	}
}
