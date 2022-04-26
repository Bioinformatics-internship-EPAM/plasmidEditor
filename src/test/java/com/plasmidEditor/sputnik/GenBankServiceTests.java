package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.repositories.GenBankRepository;

import com.plasmidEditor.sputnik.services.GenBankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenBankServiceTests {
    @Autowired
    private GenBankService service;

    @Autowired
    private GenBankRepository repository;

    @BeforeEach
    void clearDatabase() {
        repository.deleteAll();
    }

    @Test
    void createTest() {
        GenBankDTO obj = new GenBankDTO("a", "1", "file");
        GenBankDTO savedObj = service.add(obj);
        assertNotNull(savedObj);
        assertEquals(obj, savedObj);
    }

    @Test
    void readTest() {
        GenBankDTO savedObj = service.add(new GenBankDTO("a", "1", "file"));
        GenBankDTO readObj = service.get("a", "1");
        assertEquals(readObj, savedObj);
    }

    @Test
    void updateTest() {
        service.add(new GenBankDTO("a", "1", "file1"));
        GenBankDTO readObj = service.get("a", "1");
        readObj.setFile("file2");
        service.update(readObj);

        GenBankDTO updatedObj = service.get("a", "1");
        assertEquals(readObj.getFile(), updatedObj.getFile());
    }

    @Test
    void deleteTest() {
        service.add(new GenBankDTO("a", "1", "file1"));
        service.add(new GenBankDTO("b", "1", "file2"));
        service.add(new GenBankDTO("c", "1", "file3"));

        service.delete("a", "1");
        assertEquals(2, service.getAll().size());
        assertNull(service.get("a", "1"));

        service.deleteAll();
        assertEquals(0, service.getAll().size());
    }
}
