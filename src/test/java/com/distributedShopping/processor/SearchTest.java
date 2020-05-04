package com.distributedShopping.processor;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.distributedShopping.database.RelationalDatabase;
import com.distributedShopping.database.TestEnvironment;

import org.junit.*;
import org.testcontainers.containers.PostgreSQLContainer;

public class SearchTest {

    private RelationalDatabase database;

    @Rule
    public PostgreSQLContainer postgresContainer = new PostgreSQLContainer();

    @Before
        public void initializeTestEnvironment() {
        String containerUrl =postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        database = new RelationalDatabase(containerUrl, username, password);
        new TestEnvironment(database);
    }

    @Test
    public void exactProductMatchSearch() throws Exception {
        
        String searchString = "Wine - Vineland Estate Semi - Dry";
        Search search = new Search(database);
        List<SearchResult> searchResults = search.go(searchString);
        int expectedResultsSize = 100;
        assertEquals(expectedResultsSize, searchResults.size());

        SearchResult bestMatch = searchResults.get(0);
        assertEquals(searchString, bestMatch.product.title);
        
        Double expectedSimilarity = 1.0;
        assertEquals(expectedSimilarity, bestMatch.similarity);

        Integer expectedBrand = 98;
        assertEquals(expectedBrand, bestMatch.product.brandid);

        Integer expectedId = 791;
        assertEquals(expectedId, bestMatch.product.id);
    }
    
}
