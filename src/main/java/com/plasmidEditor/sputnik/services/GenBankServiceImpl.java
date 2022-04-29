package com.plasmidEditor.sputnik.services;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.entities.GenBankEntity;
import com.plasmidEditor.sputnik.repositories.GenBankRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenBankServiceImpl implements GenBankService {
    @Autowired
    private GenBankRepository repository;

    final private ModelMapper mapper = new ModelMapper();

    @Override
    public GenBankDTO save(GenBankDTO object) {
        GenBankEntity entity = mapper.map(object, GenBankEntity.class);
        return mapper.map(repository.save(entity), GenBankDTO.class);
    }

    @Override
    public GenBankDTO get(Long id) throws EntityNotFoundException{
        Optional<GenBankEntity> entity = repository.findById(id);

        if (entity.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return mapper.map(entity.get(), GenBankDTO.class);
    }

    @Override
    public GenBankDTO get(String accession, String version) throws EntityNotFoundException{
        Optional<GenBankEntity> entity = repository.findByAccessionAndVersion(accession, version);

        if (entity.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return mapper.map(entity.get(), GenBankDTO.class);
    }
}
