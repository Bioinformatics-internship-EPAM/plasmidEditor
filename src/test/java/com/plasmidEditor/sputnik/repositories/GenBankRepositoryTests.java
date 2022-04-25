package com.plasmidEditor.sputnik.repositories;

import com.plasmidEditor.sputnik.entities.GenBankEntity;
import com.plasmidEditor.sputnik.entities.GenBankId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenBankRepositoryTests{
    @Autowired
    GenBankRepository repository;

    @Test
    void createTest() {
        GenBankEntity entity = new GenBankEntity("a", "1", "file");
        GenBankEntity savedEntity = repository.save(entity);
        assertNotNull(savedEntity);
        assertEquals(entity.getAccession(), savedEntity.getAccession());
        assertEquals(entity.getVersion(), savedEntity.getVersion());
        assertEquals(entity.getFile(), savedEntity.getFile());
    }

    @Test
    void readTest() {
        List<GenBankEntity> readEntities = repository.findAll();
        assertEquals(0, readEntities.size());

        List<GenBankEntity> savedEntities = new ArrayList<>();
        savedEntities.add(repository.save(new GenBankEntity("a", "1", "file1")));
        savedEntities.add(repository.save(new GenBankEntity("b", "2", "file2")));

        readEntities = repository.findAll();

        assertEquals(savedEntities.size(), readEntities.size());

        for (int i = 0; i < readEntities.size(); i++) {
            assertEquals(savedEntities.get(i).getAccession(), readEntities.get(i).getAccession());
            assertEquals(savedEntities.get(i).getVersion(), readEntities.get(i).getVersion());
            assertEquals(savedEntities.get(i).getFile(), readEntities.get(i).getFile());
        }
    }

    @Test
    void updateTest() {
        repository.save(new GenBankEntity("a", "1", "file1"));
        Optional<GenBankEntity> entity = repository.findById(new GenBankId("a", "1"));
        entity.get().setFile("file2");

        Optional<GenBankEntity> updatedEntity = repository.findById(new GenBankId("a", "1"));
        assertEquals(updatedEntity.get().getFile(), entity.get().getFile());
    }

    @Test
    void deleteTest() {
        repository.save(new GenBankEntity("a", "1", "file1"));
        repository.save(new GenBankEntity("b", "2", "file2"));
        repository.save(new GenBankEntity("c", "3", "file3"));

        repository.deleteById(new GenBankId("a", "1"));

        List<GenBankEntity> readEntities = repository.findAll();
        assertEquals(readEntities.size(), 2);

        repository.deleteAll();

        readEntities = repository.findAll();
        assertEquals(readEntities.size(), 0);
    }
}
