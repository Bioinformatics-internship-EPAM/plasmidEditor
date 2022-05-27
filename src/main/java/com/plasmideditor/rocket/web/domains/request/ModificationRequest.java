package com.plasmideditor.rocket.web.domains.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModificationRequest {
    private Integer startPosition;
    private String sequence;
    private String fileId;
    private String fileVersion;
}
