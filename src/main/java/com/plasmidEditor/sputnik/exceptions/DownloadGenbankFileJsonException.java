package com.plasmidEditor.sputnik.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DownloadGenbankFileJsonException {
    private String downloadStatus;
    private String errorMessage;
}
