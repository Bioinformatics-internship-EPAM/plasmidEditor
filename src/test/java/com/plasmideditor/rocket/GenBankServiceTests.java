package com.plasmideditor.rocket;

import com.plasmideditor.rocket.services.GenBankService;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize"})
public class GenBankServiceTests extends PostgresTestContainer{

    private final GenBankService service;

    private GenBankData uploadObj1;

    private GenBankData uploadObj2;

    private static final String GEN_BANK_ACCESSION = "test_accession";
    private static final String GEN_BANK_VERSION = "test_version";
    private static final String GEN_BANK_FILE = "test_file";

    private static final String GEN_BANK_VERSION_2 = "test_version_2";
    private static final String GEN_BANK_ACCESSION_2 = "test_accession_2";
    private static final String GEN_BANK_FILE_2 = "test_file_2";

    public GenBankServiceTests(GenBankService service) {
        this.service = service;
    }

    @Before
    public void setUp() {
        this.uploadObj1 = service.upload(new GenBankData(1L, GEN_BANK_ACCESSION,
                GEN_BANK_VERSION, GEN_BANK_FILE));
        this.uploadObj2 = service.upload(new GenBankData(2L, GEN_BANK_ACCESSION_2,
                GEN_BANK_VERSION_2, GEN_BANK_FILE_2));
    }

    @Test
    void uploadTest() {
        assertNotNull(uploadObj1);
        assertEquals(uploadObj1.getAccession(), GEN_BANK_ACCESSION);
        assertEquals(uploadObj1.getVersion(), GEN_BANK_VERSION);
        assertEquals(uploadObj1.getFile(), GEN_BANK_FILE);

        assertNotNull(uploadObj2);
        assertEquals(uploadObj2.getAccession(), GEN_BANK_ACCESSION_2);
        assertEquals(uploadObj2.getVersion(), GEN_BANK_VERSION_2);
        assertEquals(uploadObj2.getFile(), GEN_BANK_FILE_2);
    }

    @Test
    void getTest() {
        GenBankData getObj1 = service.get(GEN_BANK_ACCESSION, GEN_BANK_VERSION);
        GenBankData getObj2 = service.get(GEN_BANK_ACCESSION_2, GEN_BANK_VERSION_2);

        assertNotNull(getObj1);
        assertEquals(getObj1.getAccession(), GEN_BANK_ACCESSION);
        assertEquals(getObj1.getVersion(), GEN_BANK_VERSION);
        assertEquals(getObj1.getFile(), GEN_BANK_FILE);

        assertNotNull(getObj2);
        assertEquals(getObj2.getAccession(), GEN_BANK_ACCESSION_2);
        assertEquals(getObj2.getVersion(), GEN_BANK_VERSION_2);
        assertEquals(getObj2.getFile(), GEN_BANK_FILE_2);
    }

    @Test
    void updateTest() {
        GenBankData getObj = service.get(GEN_BANK_ACCESSION, GEN_BANK_VERSION);
        getObj.setFile("new_test_file");
        service.upload(getObj);

        GenBankData updatedObj = service.get(GEN_BANK_ACCESSION, GEN_BANK_VERSION);
        assertEquals(getObj.getFile(), updatedObj.getFile());
    }

    @After
    public void tearDown() {
        service.delete(1L);
        service.delete(2L);
    }

}
