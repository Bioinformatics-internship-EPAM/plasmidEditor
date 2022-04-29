package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class GenBankServiceImplTests extends PostgreSQLTestContainer {
    @Autowired
    private GenBankServiceImpl service;

    @Test
    @Transactional
    void createTest() {
        GenBankDTO obj = new GenBankDTO("a", "1", "file");
        GenBankDTO savedObj = service.save(obj);
        assertNotNull(savedObj);
        assertEquals(obj.getAccession(), savedObj.getAccession());
        assertEquals(obj.getVersion(), savedObj.getVersion());
        assertEquals(obj.getFile(), savedObj.getFile());
    }

    @Test
    @Transactional
    void readTest() {
        GenBankDTO savedObj = service.save(new GenBankDTO("a", "1", "file"));
        GenBankDTO readObj = service.get("a", "1");
        assertEquals(readObj, savedObj);

        savedObj = service.save(new GenBankDTO("b", "1", "file"));
        readObj = service.get(2L);
        assertEquals(readObj, savedObj);
    }

    @Test
    @Transactional
    void updateTest() {
        service.save(new GenBankDTO("a", "1", "file"));
        GenBankDTO readObj = service.get("a", "1");
        readObj.setFile("new_file");
        service.save(readObj);

        GenBankDTO updatedObj = service.get("a", "1");
        assertEquals(readObj.getFile(), updatedObj.getFile());
    }
}
