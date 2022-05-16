package com.plasmidEditor.sputnik.controllers;

import lombok.*;

@Data
@AllArgsConstructor
public class ErrorResponseBody {
	private String error;
	private String message;
}
