package com.plasmideditor.rocket;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class GenBankData implements Serializable {

    private Long genbankId;

    private String accession;

    private String version;

    private String file;
}
