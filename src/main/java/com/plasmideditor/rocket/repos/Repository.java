package com.plasmideditor.rocket.repos;


import com.plasmideditor.rocket.postgres.GenBankTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<GenBankTable, Long> {

}
