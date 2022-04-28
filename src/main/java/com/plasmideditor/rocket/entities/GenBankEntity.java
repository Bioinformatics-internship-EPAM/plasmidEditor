package com.plasmideditor.rocket.entities;

import javax.persistence.*;

@Entity
@Table(
        schema = "genbank",
        name = "genbanks"
)
@PrimaryKeyJoinColumn(name = "genbank_id")
public class GenBankEntity {

    @Id
    @GeneratedValue
    @Column(name = "genbank_id")
    private Long genbank_id;

    @Column(name = "accession")
    private String accession;

    @Column(name = "version")
    private String version;

    @Column(name = "file")
    private String file;

    public Long getGenbank_id() {
        return genbank_id;
    }

    public void setGenbank_id(Long genbank_id) {
        this.genbank_id = genbank_id;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
