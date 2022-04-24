package com.plasmidEditor.sputnik.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Table(name = "genbank")
@IdClass(GenBankEntityKey.class)
public class GenBankEntity {
    @Id
    private String accession;
    @Id
    private String version;

    private String file;

    public GenBankEntity() {

    }

    public GenBankEntity(String accession, String version, String file) {
        this.accession = accession;
        this.version = version;
        this.file = file;
    }

    public String getAccession() {
        return this.accession;
    }

    public String getVersion() {
        return this.version;
    }

    public String getFile() {
        return this.file;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "GenBankEntity{" +
                "accession='" + accession + '\'' +
                ", version='" + version + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}
