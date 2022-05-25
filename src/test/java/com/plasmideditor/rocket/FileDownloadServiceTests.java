package com.plasmideditor.rocket;

import com.plasmideditor.rocket.services.GenBankService;
import com.plasmideditor.rocket.web.service.FileDownloadService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileDownloadServiceTests {

    private static final Long TEST_ID_1 = 1L;
    private static final Long TEST_ID_2 = 2L;
    private static final String TEST_ACCESSION = "test_accession";
    private static final String TEST_VERSION_OLD = "test_version_old";
    private static final String TEST_VERSION_NEW = "test_version_new";
    private static final String TEST_FILE_CONTENT = "test_file";

    private static FileDownloadService fileDownloadService;

    @BeforeAll
    static void setUp() {
        GenBankService mockGenBankService = Mockito.mock(GenBankService.class);

        Mockito.when(mockGenBankService.getLatest(TEST_ACCESSION))
                .thenReturn(new GenBankData(TEST_ID_2,TEST_ACCESSION, TEST_VERSION_NEW, TEST_FILE_CONTENT));
        Mockito.when(mockGenBankService.get(TEST_ACCESSION, TEST_VERSION_OLD))
                .thenReturn(new GenBankData(TEST_ID_1,TEST_ACCESSION, TEST_VERSION_OLD, TEST_FILE_CONTENT));

        fileDownloadService = new FileDownloadService(mockGenBankService);
    }

    @Test
    void fileDownloadLatestTest() {
        GenBankData data = fileDownloadService.downloadFile(TEST_ACCESSION, null);
        assertEquals(TEST_ACCESSION, data.getAccession());
        assertEquals(TEST_VERSION_NEW, data.getVersion());
    }

    @Test
    void fileDownloadSpecifiedVersionTest() {
        GenBankData data = fileDownloadService.downloadFile(TEST_ACCESSION, TEST_VERSION_OLD);
        assertEquals(TEST_ACCESSION, data.getAccession());
        assertEquals(TEST_VERSION_OLD, data.getVersion());
    }

}
