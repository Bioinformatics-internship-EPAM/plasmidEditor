package com.plasmidEditor.sputnik.entities;

import java.io.Serializable;
import java.util.Objects;

public class GenBankEntityKey implements Serializable {
    private String accession;
    private String version;

    public GenBankEntityKey() {

    }

    public GenBankEntityKey(String accession, String version) {
        this.accession = accession;
        this.version = version;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GenBankEntityKey genBankEntityKey = (GenBankEntityKey) object;
        return accession.equals(genBankEntityKey.accession) && version.equals(genBankEntityKey.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accession, version);
    }
}