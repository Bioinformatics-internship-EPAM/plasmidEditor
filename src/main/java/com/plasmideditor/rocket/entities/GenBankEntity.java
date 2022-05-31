package com.plasmideditor.rocket.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(
        schema = EntitiesConst.GENBANK_SCHEMA,
        name = "genbanks"
)
@PrimaryKeyJoinColumn(name = "genbank_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
