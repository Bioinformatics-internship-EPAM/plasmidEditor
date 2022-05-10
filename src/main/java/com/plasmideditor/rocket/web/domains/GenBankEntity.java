package com.plasmideditor.rocket.web.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(
        schema = "genbank",
        name = "genbanks",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"accession", "version"})}
)
@PrimaryKeyJoinColumn(name = "genbank_id")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public GenBankEntity(String accession, String version, String file) {
        this.accession = accession;
        this.version = version;
        this.file = file;
    }
}