package com.plasmidEditor.sputnik;

import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = {GenBankServiceImplTests.Initializer.class})
@Testcontainers
class GenBankServiceImplTests {
    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14.2")
            .withDatabaseName("test_plasmid_database")
            .withUsername("user")
            .withPassword("password");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + container.getJdbcUrl(),
                    "spring.datasource.username=" + container.getUsername(),
                    "spring.datasource.password=" + container.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

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

    @Test
    @Transactional
    void deleteTest() {
        service.save(new GenBankDTO("a", "1", "file1"));
        service.save(new GenBankDTO("b", "1", "file2"));

        service.delete("a", "1");
        assertEquals(1, service.getAll().size());
        assertThrows(EntityNotFoundException.class, () -> service.get("a", "1"));
    }
}
