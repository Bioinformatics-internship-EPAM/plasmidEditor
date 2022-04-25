package com.plasmidEditor.sputnik.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GenBankId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accession;
    private String version;

    public GenBankId() {}

    public GenBankId(String accession, String version) {
        this.accession = accession;
        this.version = version;
    }

    public String getAccession() {
        return this.accession;
    }

    public String getVersion() {
        return this.version;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GenBankId id = (GenBankId) object;
        return accession.equals(id.accession) && version.equals(id.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accession, version);
    }

    @Override
    public String toString() {
        return "GenBankId{" +
                "accession='" + accession + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}