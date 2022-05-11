package com.plasmidEditor.sputnik.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.plasmidEditor.sputnik.entities.GenBankEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenBankRepository extends JpaRepository<GenBankEntity, Long> {
    Optional<GenBankEntity> findByAccessionAndVersion(String accession, String version);

    List<GenBankEntity> findByAccessionOrderByVersionDesc(String accession);
}
