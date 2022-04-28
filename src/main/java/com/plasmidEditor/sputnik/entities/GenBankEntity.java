package com.plasmidEditor.sputnik.entities;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "genbank", name = "genbanks")
public class GenBankEntity {
    @Id
    @GeneratedValue
    private Long genbankId;

    private String accession;
    private String version;
    private String file;
}
