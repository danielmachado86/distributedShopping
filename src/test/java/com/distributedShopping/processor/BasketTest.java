package com.distributedShopping.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.distributedShopping.database.RelationalDatabase;
import com.distributedShopping.database.TestEnvironment;

import org.junit.*;

import org.testcontainers.containers.PostgreSQLContainer;

public class BasketTest {

    private RelationalDatabase database;

    @Rule
    public PostgreSQLContainer postgresContainer = new PostgreSQLContainer();

    @Before
    public void initializeTestEnvironment() {
        String containerUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        database = new RelationalDatabase(containerUrl, username, password);
        new TestEnvironment(database);
    }

    @Test
    public void addNewBasketItemToDatabase() {

        Integer addressId = 1;
        Integer productId = 2;
        Double selectedQuantity = 10.0;
        BasketData basketItem = new BasketData(addressId, productId, selectedQuantity);
        BasketService basket = new BasketService(database);
        Boolean result = basket.insert(basketItem);

        assertTrue(result);

    }

    @Test
    public void updateBasketItem() {

        Integer addressId = 1;
        Integer productId = 1;
        Double selectedQuantity = 10.0;
        BasketData basketItem = new BasketData(addressId, productId, selectedQuantity);
        BasketService basket = new BasketService(database);
        BasketData result = basket.update(basketItem);
        
        Integer expectedAddressId = 1;
        Integer expectedProductId = 1;
        Double expectedSelectedQuantity = 10.0;

        assertEquals(expectedAddressId, result.addressId);
        assertEquals(expectedProductId, result.addressId);
        assertEquals(expectedSelectedQuantity, result.selectedQuantity);

    }

    @Test
    public void retrieveBasket() throws Exception {
        
        BasketService basketService = new BasketService(database);
        List<BasketData> basket = basketService.loadBasket(1);
        int expectedResultsSize = 1;
        assertEquals(expectedResultsSize, basket.size());

        BasketData firstItem = basket.get(0);        
        Double expectedSelectedQuantity = 1.0;
        assertEquals(expectedSelectedQuantity, firstItem.selectedQuantity);
    }

}