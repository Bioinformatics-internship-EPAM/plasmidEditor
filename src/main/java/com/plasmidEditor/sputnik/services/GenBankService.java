package com.plasmidEditor.sputnik.services;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.entities.GenBankEntity;
import com.plasmidEditor.sputnik.repositories.GenBankRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GenBankService {
    @Autowired
    GenBankRepository repository;

    private final ModelMapper mapper = new ModelMapper();

    public GenBankDTO add(GenBankDTO object) {
        GenBankEntity entity = mapper.map(object, GenBankEntity.class);
        return mapper.map(repository.save(entity), GenBankDTO.class);
    }

    public GenBankDTO get(String accession, String version) {
        Optional<GenBankEntity> entity = repository.findByAccessionAndVersion(accession, version);
        return entity.map(x -> mapper.map(x, GenBankDTO.class)).orElse(null);
    }

    public List<GenBankDTO> getAll() {
        List<GenBankDTO> objects = new ArrayList<>();
        List<GenBankEntity> entities = repository.findAll();

        for (GenBankEntity entity : entities) {
            objects.add(mapper.map(entity, GenBankDTO.class));
        }

        return objects;
    }

    public GenBankDTO update(GenBankDTO object) {
        Optional<GenBankEntity> entity = repository.findByAccessionAndVersion(
                object.getAccession(), object.getVersion());
        if (entity.isPresent()) {
            entity.get().setFile(object.getFile());
            repository.save(entity.get());
            return mapper.map(entity.get(), GenBankDTO.class);
        }

        return add(object);
    }

    public void delete(String accession, String version) {
        repository.deleteByAccessionAndVersion(accession, version);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
