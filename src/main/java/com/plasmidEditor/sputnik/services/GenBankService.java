package com.plasmidEditor.sputnik.services;

import com.plasmidEditor.sputnik.entities.GenBankEntity;
import com.plasmidEditor.sputnik.entities.GenBankId;
import com.plasmidEditor.sputnik.repositories.GenBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenBankService {
    @Autowired
    GenBankRepository repository;

    public GenBankEntity add(GenBankEntity entity) {
        return repository.saveAndFlush(entity);
    }

    public List<GenBankEntity> add(List<GenBankEntity> entities) {
        return repository.saveAllAndFlush(entities);
    }

    public List<GenBankEntity> getAll() {
        return repository.findAll();
    }

    public GenBankEntity get(String accession, String version) {
        return repository.findById(new GenBankId(accession, version)).orElse(null);
    }

    public GenBankEntity update(GenBankEntity entity) {
        return repository.saveAndFlush(entity);
    }

    public void delete(String accession, String version) {
        repository.deleteById(new GenBankId(accession, version));
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
