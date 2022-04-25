package com.plasmidEditor.sputnik.services;

import com.plasmidEditor.sputnik.entities.GenBankEntity;
import com.plasmidEditor.sputnik.entities.GenBankId;
import com.plasmidEditor.sputnik.repositories.GenBankRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenBankServiceTests{
    @Autowired
    private GenBankService service;

    @Autowired
    private GenBankRepository repository;

    @Test
    void createTest() {
        GenBankEntity entity = new GenBankEntity(new GenBankId("a", "1"), "file1");
        GenBankEntity savedEntity = service.add(entity);
        assertNotNull(savedEntity);
        assertEquals(savedEntity, entity);
    }

    @Test
    void readTest() {
        List<GenBankEntity> readEntities = service.getAll();
        assertEquals(0, readEntities.size());

        GenBankEntity savedEntity = service.add(new GenBankEntity(new GenBankId("a", "1"), "file1"));
        GenBankEntity readEntity = service.get("a", "1");
        assertEquals(readEntity, savedEntity);
    }

    @Test
    void updateTest() {
        service.add(new GenBankEntity(new GenBankId("a", "1"), "file1"));
        GenBankEntity readEntity = service.get("a", "1");
        readEntity.setFile("file2");
        service.update(readEntity);

        GenBankEntity updatedEntity = service.get("a", "1");
        assertEquals(readEntity.getFile(), updatedEntity.getFile());
    }

    @Test
    void deleteTest() {
        service.add(new GenBankEntity(new GenBankId("a", "1"), "file1"));
        service.add(new GenBankEntity(new GenBankId("b", "2"), "file2"));
        service.add(new GenBankEntity(new GenBankId("c", "3"), "file3"));

        service.delete("a", "1");
        assertEquals(2, service.getAll().size());
        assertNull(service.get("a", "1"));

        service.deleteAll();
        assertEquals(0, service.getAll().size());
    }
}
