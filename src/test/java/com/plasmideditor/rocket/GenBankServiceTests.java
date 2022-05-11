package com.plasmideditor.rocket;

import com.plasmideditor.rocket.services.GenBankService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GenBankServiceTests extends PostgresTestContainer{

    @Autowired
    private final GenBankService service;

    public GenBankServiceTests(GenBankService service) {
        this.service = service;
    }

    @Test
    @Order(1)
    void uploadTest() {
        GenBankData uploadObj1 = service.upload(new GenBankData(1L, "test_accession",
                "test_version", "test_file"));
        GenBankData uploadObj2 = service.upload(new GenBankData(2L, "test_accession_2",
                "test_version_2", "test_file_2"));

        assertNotNull(uploadObj1);
        assertEquals(uploadObj1.getAccession(), "test_accession");
        assertEquals(uploadObj1.getVersion(), "test_version");
        assertEquals(uploadObj1.getFile(), "test_file");

        assertNotNull(uploadObj2);
        assertEquals(uploadObj2.getAccession(), "test_accession_2");
        assertEquals(uploadObj2.getVersion(), "test_version_2");
        assertEquals(uploadObj2.getFile(), "test_file_2");
    }

    @Test
    @Order(2)
    void getTest() {
        GenBankData getObj1 = service.get("test_accession", "test_version");
        GenBankData getObj2 = service.get("test_accession_2", "test_version_2");

        assertNotNull(getObj1);
        assertEquals(getObj1.getAccession(), "test_accession");
        assertEquals(getObj1.getVersion(), "test_version");
        assertEquals(getObj1.getFile(), "test_file");

        assertNotNull(getObj2);
        assertEquals(getObj2.getAccession(), "test_accession_2");
        assertEquals(getObj2.getVersion(), "test_version_2");
        assertEquals(getObj2.getFile(), "test_file_2");
    }

    @Test
    @Order(3)
    void updateTest() {
        GenBankData getObj = service.get("test_accession", "test_version");
        getObj.setFile("new_test_file");
        service.upload(getObj);

        GenBankData updatedObj = service.get("test_accession", "test_version");
        assertEquals(getObj.getFile(), updatedObj.getFile());
    }

}
