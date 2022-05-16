package com.plasmideditor.rocket.genbank.repository;

import com.plasmideditor.rocket.genbank.repository.domains.GenBankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenBankRepository extends JpaRepository<GenBankEntity, Long> {
    Optional<GenBankEntity> findByAccessionAndVersion(String accession, int version);
}