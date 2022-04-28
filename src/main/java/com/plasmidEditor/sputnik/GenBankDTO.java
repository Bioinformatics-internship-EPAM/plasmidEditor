package com.plasmidEditor.sputnik;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class GenBankDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long genbank_id;
    @NonNull
    private String accession;
    @NonNull
    private String version;
    @NonNull
    private String file;
}
