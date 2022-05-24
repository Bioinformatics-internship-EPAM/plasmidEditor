package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenBankServiceImplTests extends PostgreSQLTestContainer {
    @Autowired
    private transient GenBankServiceImpl service;

    @Test
    @Order(1)
    void createTest() {
        GenBankDTO savedObj1 = service.save(new GenBankDTO("a", "1", "file1"));
        GenBankDTO savedObj2 = service.save(new GenBankDTO("b", "1", "file2"));

        assertNotNull(savedObj1);
        assertEquals(savedObj1.getAccession(), "a");
        assertEquals(savedObj1.getVersion(), "1");
        assertEquals(savedObj1.getFile(), "file1");

        assertNotNull(savedObj2);
        assertEquals(savedObj2.getAccession(), "b");
        assertEquals(savedObj2.getVersion(), "1");
        assertEquals(savedObj2.getFile(), "file2");
    }

    @Test
    @Order(2)
    void readTest() {
        GenBankDTO readObj1 = service.get("a", "1");
        GenBankDTO readObj2 = service.get("b", "1");

        assertNotNull(readObj1);
        assertEquals(readObj1.getAccession(), "a");
        assertEquals(readObj1.getVersion(), "1");
        assertEquals(readObj1.getFile(), "file1");

        assertNotNull(readObj2);
        assertEquals(readObj2.getAccession(), "b");
        assertEquals(readObj2.getVersion(), "1");
        assertEquals(readObj2.getFile(), "file2");
    }

    @Test
    @Order(3)
    void updateTest() {
        GenBankDTO readObj = service.get("a", "1");
        readObj.setFile("new_file");
        service.save(readObj);

        GenBankDTO updatedObj = service.get("a", "1");
        assertEquals(readObj.getFile(), updatedObj.getFile());
    }

    @Test
    void getLatestVersionTest() {
        service.save(new GenBankDTO("NM_000266", "NM_000266.1", "file1"));
        service.save(new GenBankDTO("NM_000266", "NM_000266.5", "file5"));
        service.save(new GenBankDTO("NM_000266", "NM_000266.2", "file2"));

        GenBankDTO readObj = service.getLatestVersion("NM_000266");

        assertNotNull(readObj);
        assertEquals(readObj.getAccession(), "NM_000266");
        assertEquals(readObj.getVersion(), "NM_000266.5");
        assertEquals(readObj.getFile(), "file5");
    }
}
