package com.plasmideditor.rocket.genbank.domains.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileRequest {
    private String fileId;
    private String fileVersion;
}
