package com.plasmidEditor.sputnik.entities;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        schema = "genbank",
        name = "genbanks",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"accession", "version"})}
)
public class GenBankEntity {
    @Id
    @GeneratedValue
    private Long genbank_id;

    @Column(nullable = false)
    private String accession;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private String file;
}
