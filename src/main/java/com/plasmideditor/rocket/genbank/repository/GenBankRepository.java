package com.plasmideditor.rocket.genbank.repository;

import com.plasmideditor.rocket.web.domains.GenBankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenBankRepository extends JpaRepository<GenBankEntity, Long> {
    Optional<GenBankEntity> findByAccessionIdAndVersion(String id, String version);
}
