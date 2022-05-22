package com.plasmideditor.rocket.database.repositories;


import com.plasmideditor.rocket.database.entities.GenBankEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenBankRepository extends JpaRepository<GenBankEntity, Long> {
    Optional<GenBankEntity> findByAccessionAndVersion(String accession, String version);
}
