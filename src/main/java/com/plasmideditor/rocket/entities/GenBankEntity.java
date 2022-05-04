package com.plasmideditor.rocket.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(
        schema = "genbank",
        name = "genbanks",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"accession", "version"})}
)
@PrimaryKeyJoinColumn(name = "genbank_id")
@Data
@Builder
public class GenBankEntity {

    @Id
    @GeneratedValue
    @Column(name = "genbank_id")
    private Long genbankId;

    @Column(name = "accession")
    private String accession;

    @Column(name = "version")
    private String version;

    @Column(name = "file")
    private String file;

}
