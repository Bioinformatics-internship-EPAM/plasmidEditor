package com.plasmidEditor.sputnik.controllers;

import lombok.*;

@Data
@AllArgsConstructor
public class JsonError {
	private String error;
	private String message;
}
