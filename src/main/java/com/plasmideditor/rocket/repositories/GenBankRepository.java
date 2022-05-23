package com.plasmideditor.rocket.repositories;


import com.plasmideditor.rocket.entities.GenBankEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenBankRepository extends JpaRepository<GenBankEntity, Long> {
    Optional<GenBankEntity> findByAccessionAndVersion(String accession, String version);
    Optional<GenBankEntity> findByAccessionOrderByVersionDesc(String accession);
}
