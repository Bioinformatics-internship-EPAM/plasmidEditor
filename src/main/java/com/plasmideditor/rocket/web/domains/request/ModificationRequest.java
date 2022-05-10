package com.plasmideditor.rocket.web.domains.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModificationRequest {
    private SequenceInfoRequest sequenceInfoRequest;
    private FileRequest fileRequest;
}
