package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.download.GenbankFileDownloadService;
import com.plasmidEditor.sputnik.exceptions.DownloadGenbankFileException;
import com.plasmidEditor.sputnik.services.GenBankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

import static com.plasmidEditor.sputnik.utils.Constants.DEFAULT_VERSION_VALUE;
import static org.junit.jupiter.api.Assertions.*;

class GenbankFileDownloadServiceTests {
    private static final String TEST_ACCESSION = "G87I_567158";
    private static final String TEST_VERSION = "G87I_567158.1";
    private static final String TEST_FILE_CONTENT = "file";

    private static GenbankFileDownloadService fileDownloadService;

    @BeforeAll
    static void initAll() {
        GenBankService mockGenBankService = Mockito.mock(GenBankService.class);

        Mockito.when(mockGenBankService.getLatestVersion(TEST_ACCESSION)).thenReturn(new GenBankDTO());
        Mockito.when(mockGenBankService.get(TEST_ACCESSION, TEST_VERSION))
                .thenReturn(new GenBankDTO(TEST_ACCESSION, TEST_VERSION, TEST_FILE_CONTENT));

        fileDownloadService = new GenbankFileDownloadService(mockGenBankService);
    }

    @Test
    void fileDownloadLatestVersionSuccessfullyTest() {
        assertDoesNotThrow(() -> fileDownloadService.downloadFile(TEST_ACCESSION, DEFAULT_VERSION_VALUE));
    }

    @Test
    void fileDownloadSpecifiedVersionSuccessfullyTest() {
        try {
            GenBankDTO dto = fileDownloadService.downloadFile(TEST_ACCESSION, TEST_VERSION);
            assertEquals(TEST_ACCESSION, dto.getAccession());
            assertEquals(TEST_VERSION, dto.getVersion());
        } catch (DownloadGenbankFileException e) {
            fail();
        }
    }

    @Test
    void fileConvertToByteArraySuccessfullyTest() {
        assertDoesNotThrow(() -> fileDownloadService.convertGenbankDTOToByteArray(new GenBankDTO()));
    }
}
