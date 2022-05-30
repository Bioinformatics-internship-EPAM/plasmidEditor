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

    private final transient String FILE1 = "file1";
    private final transient String FILE2 = "file2";

    @Test
    @Order(1)
    void createTest() {
        GenBankDTO savedObj1 = service.save(new GenBankDTO("a", "1", "file1"));
        GenBankDTO savedObj2 = service.save(new GenBankDTO("b", "1", "file2"));

        assertNotNull(savedObj1);
        assertEquals(savedObj1.getAccession(), "a");
        assertEquals(savedObj1.getVersion(), "1");
        assertEquals(savedObj1.getFile(), FILE1);

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
        assertEquals(readObj1.getFile(), FILE1);

        assertNotNull(readObj2);
        assertEquals(readObj2.getAccession(), "b");
        assertEquals(readObj2.getVersion(), "1");
        assertEquals(readObj2.getFile(), FILE2);
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
        String ACCEESSION = "NM_000266";
        service.save(new GenBankDTO(ACCEESSION, "NM_000266.1", FILE1));
        service.save(new GenBankDTO(ACCEESSION, "NM_000266.5", "file5"));
        service.save(new GenBankDTO(ACCEESSION, "NM_000266.2", FILE2));

        GenBankDTO readObj = service.getLatestVersion(ACCEESSION);

        assertNotNull(readObj);
        assertEquals(readObj.getAccession(), ACCEESSION);
        assertEquals(readObj.getVersion(), "NM_000266.5");
        assertEquals(readObj.getFile(), "file5");
    }
}
