package com.company.processor;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.company.database.Database;
import com.company.database.TestEnvironment;

import org.junit.*;
import org.testcontainers.containers.PostgreSQLContainer;

public class SearchTest {

    private Database database;

    @Rule
    public PostgreSQLContainer postgresContainer = new PostgreSQLContainer();

    @Before
        public void initializeTestEnvironment() {
        String containerUrl =postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        database = new Database(containerUrl, username, password);
        new TestEnvironment(database);
    }

    @Test
    public void exactProductMatchSearch() throws Exception {
        
        String searchString = "Wine - Vineland Estate Semi - Dry";
        Search search = new Search(database);
        List<SearchResult> searchResults = search.go(searchString);
        assertEquals(searchResults.size(), 85);

        SearchResult bestMatch = searchResults.get(0);
        String expectedResult = searchString;
        assertEquals(bestMatch.getTitle(), expectedResult);
        
        Double expectedSimilarity = 1.0;
        assertEquals(bestMatch.getSimilarity(), expectedSimilarity);
    }
    
}
