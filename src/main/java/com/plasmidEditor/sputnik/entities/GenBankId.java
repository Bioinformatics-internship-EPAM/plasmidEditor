package com.plasmidEditor.sputnik.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GenBankId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String accession;

    @Column(nullable = false)
    private String version;
}