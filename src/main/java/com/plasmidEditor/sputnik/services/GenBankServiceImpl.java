package com.plasmidEditor.sputnik.services;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.entities.GenBankEntity;
import com.plasmidEditor.sputnik.exceptions.GenBankNotFoundException;
import com.plasmidEditor.sputnik.repositories.GenBankRepository;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Getter
@Service
public class GenBankServiceImpl implements GenBankService {
    @Autowired
    private GenBankRepository repository;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    public GenBankDTO save(GenBankDTO object) {
        GenBankEntity entity = mapper.map(object, GenBankEntity.class);
        return mapper.map(repository.save(entity), GenBankDTO.class);
    }

    @Override
    public GenBankDTO get(Long id) throws GenBankNotFoundException{
        return repository.findById(id)
                .map(x -> mapper.map(x, GenBankDTO.class))
                .orElseThrow(() -> new GenBankNotFoundException(id));

    }

    @Override
    public GenBankDTO get(String accession, String version) throws GenBankNotFoundException{
        return repository.findByAccessionAndVersion(accession, version)
                .map(x -> mapper.map(x, GenBankDTO.class))
                .orElseThrow(() -> new GenBankNotFoundException(accession, version));
    }
}
