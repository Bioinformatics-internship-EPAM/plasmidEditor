package com.plasmideditor.rocket;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public class PostgresTestContainer {

    @Bean
    public PostgreSQLContainer postgreSQLContainer() {
        final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14.2");
        postgreSQLContainer.start();
        return postgreSQLContainer;
    }

    @Bean
    public DataSource datasource(PostgreSQLContainer postgreSQLContainer) {
        return DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();
    }

}
