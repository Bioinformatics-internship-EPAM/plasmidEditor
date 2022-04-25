package com.plasmidEditor.sputnik.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EmbeddedId;

@Entity
@Table(name = "genbank")
public class GenBankEntity {
    @EmbeddedId
    private GenBankId id;

    private String file;

    public GenBankEntity() {}

    public GenBankEntity(GenBankId id, String file) {
        this.id = id;
        this.file = file;
    }

    public GenBankId getId() {
        return this.id;
    }

    public String getFile() {
        return this.file;
    }

    public void setId(GenBankId id) {
        this.id = id;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "GenBankEntity{" +
                "id=" + id +
                ", file='" + file + '\'' +
                '}';
    }
}
