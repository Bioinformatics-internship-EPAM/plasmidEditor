package com.plasmideditor.rocket.genbank.domains.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class SequenceInfoRequest {
    private Integer startPosition;
    private String sequence;
}
