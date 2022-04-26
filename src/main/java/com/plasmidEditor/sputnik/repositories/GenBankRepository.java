package com.plasmidEditor.sputnik.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.plasmidEditor.sputnik.entities.GenBankEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface GenBankRepository extends JpaRepository<GenBankEntity, Long> {
    Optional<GenBankEntity> findByAccessionAndVersion(String accession, String version);

    @Transactional
    void deleteByAccessionAndVersion(String accession, String version);
}
