package com.plasmidEditor.sputnik.editor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditorParameters {
    private String subsequence;
    private int position;
    private String fileContent;
    private int cutSize;
}
