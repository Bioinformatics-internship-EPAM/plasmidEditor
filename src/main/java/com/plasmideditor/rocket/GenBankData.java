package com.plasmideditor.rocket;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenBankData implements Serializable {

    private static final long serialVersionUID = -5725433555425134935L;

    private Long genbankId;

    private String accession;

    private String version;

    private String file;
}
