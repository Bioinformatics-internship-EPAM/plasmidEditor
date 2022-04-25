package com.plasmidEditor.sputnik.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.plasmidEditor.sputnik.entities.GenBankEntity;
import com.plasmidEditor.sputnik.entities.GenBankId;

@Repository
public interface GenBankRepository extends JpaRepository<GenBankEntity, GenBankId> {

}
