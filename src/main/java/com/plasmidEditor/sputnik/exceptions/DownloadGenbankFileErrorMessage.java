package com.plasmidEditor.sputnik.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DownloadGenbankFileErrorMessage {
    private String downloadStatus;
    private String errorMessage;
}
