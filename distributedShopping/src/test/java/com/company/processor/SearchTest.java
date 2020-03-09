package com.company.processor;

import static org.junit.Assert.assertEquals;

import org.junit.*;

import com.company.database.TestDatabase;


public class SearchTest {
    @Before
    public void initializeDatabase() {
        final TestDatabase database = new TestDatabase();
        database.connect();
        database.initialize();
    }

    @Test
    public void processSearchStringTest() throws Exception {

        assertEquals(false, true);
    }
}
