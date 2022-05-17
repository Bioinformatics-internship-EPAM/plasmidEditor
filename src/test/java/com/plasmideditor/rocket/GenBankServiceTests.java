package com.plasmideditor.rocket;

import com.plasmideditor.rocket.services.GenBankService;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GenBankServiceTests extends PostgresTestContainer{

    private final GenBankService service;

    private GenBankData uploadObj1;

    private GenBankData uploadObj2;

    public GenBankServiceTests(GenBankService service) {
        this.service = service;
    }

    @Before
    public void setUp() {
        this.uploadObj1 = service.upload(new GenBankData(1L, "test_accession",
                "test_version", "test_file"));
        this.uploadObj2 = service.upload(new GenBankData(2L, "test_accession_2",
                "test_version_2", "test_file_2"));
    }

    @Test
    void uploadTest() {
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
    void updateTest() {
        GenBankData getObj = service.get("test_accession", "test_version");
        getObj.setFile("new_test_file");
        service.upload(getObj);

        GenBankData updatedObj = service.get("test_accession", "test_version");
        assertEquals(getObj.getFile(), updatedObj.getFile());
    }

    @After
    public void tearDown() {
        service.delete(1L);
        service.delete(2L);
    }

}
