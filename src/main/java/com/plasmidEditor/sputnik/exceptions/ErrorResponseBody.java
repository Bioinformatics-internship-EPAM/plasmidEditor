package com.plasmidEditor.sputnik.exceptions;

import lombok.*;

@Data
@AllArgsConstructor
public class ErrorResponseBody {
	private String error;
	private String message;
}
