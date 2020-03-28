package com.company.processor;

import static org.junit.Assert.assertEquals;

import com.company.database.DatabaseConfiguration;
import com.company.database.TestEnvironment;

import org.junit.*;
import org.testcontainers.containers.PostgreSQLContainer;

public class SearchTest {

    private DatabaseConfiguration database;

    @Rule
    public PostgreSQLContainer postgresContainer = new PostgreSQLContainer();

    @Before
        public void initializeTestEnvironment() {
        String containerUrl =postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        database = new DatabaseConfiguration(containerUrl, username, password);
        new TestEnvironment(database);
    }

    @Test
    public void processSearchStringTest() throws Exception {
        
        Search search = new Search(database);
        search.go("Vineland Estate Semi - Dry");
        assertEquals(1, 0);
    }
}
