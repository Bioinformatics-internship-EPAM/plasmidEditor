package com.plasmidEditor.sputnik.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EmbeddedId;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(schema = "genbank", name = "genbanks")
public class GenBankEntity {
    @EmbeddedId
    private GenBankId id;
    private String file;
}
