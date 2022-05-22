package com.plasmideditor.rocket.database.services;

import com.plasmideditor.rocket.GenBankData;
import com.plasmideditor.rocket.database.entities.GenBankEntity;
import com.plasmideditor.rocket.database.exceptions.GenBankFileNotFoundException;
import com.plasmideditor.rocket.database.mappers.GenBankMapper;
import com.plasmideditor.rocket.database.repositories.GenBankRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GenBankService {

    private final GenBankRepository repository;

    private final GenBankMapper mapper;

    public GenBankData upload(GenBankData data){
        GenBankEntity entity = mapper.map(data, GenBankEntity.class);
        return mapper.map(repository.save(entity), GenBankData.class);
    }

    public GenBankData get(Long id){
        return repository.findById(id)
                .map(x -> mapper.map(x, GenBankData.class))
                .orElseThrow(() -> new GenBankFileNotFoundException(id));
    }

    public GenBankData get(String accession, String version){
        return repository.findByAccessionAndVersion(accession, version)
                .map(x -> mapper.map(x, GenBankData.class))
                .orElseThrow(() -> new GenBankFileNotFoundException(accession, version));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
