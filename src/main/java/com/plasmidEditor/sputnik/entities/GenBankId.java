package com.plasmidEditor.sputnik.entities;

import java.io.Serializable;
import java.util.Objects;

public class GenBankId implements Serializable {
    private String accession;
    private String version;

    public GenBankId() {}

    public GenBankId(String accession, String version) {
        this.accession = accession;
        this.version = version;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GenBankId genBankId = (GenBankId) object;
        return accession.equals(genBankId.accession) && version.equals(genBankId.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accession, version);
    }
}