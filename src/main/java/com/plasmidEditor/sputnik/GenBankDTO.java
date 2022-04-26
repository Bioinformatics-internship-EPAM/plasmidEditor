package com.plasmidEditor.sputnik;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenBankDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accession;
    private String version;
    private String file;
}
