package com.plasmideditor.rocket.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Table(name = "genbank")
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "accession"),
        @PrimaryKeyJoinColumn(name = "version")
})
public class GenBankEntity {

    @Column(name = "accession")
    private String accession;

    @Column(name = "version")
    private String firstName;

    @Column(name = "file")
    private String file;

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
