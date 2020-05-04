package com.distributedShopping.processor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.distributedShopping.database.RelationalDatabase;
import com.distributedShopping.database.SQL.Condition;
import com.distributedShopping.database.SQL.Equal;
import com.distributedShopping.database.SQL.OR;
import com.distributedShopping.database.SQL.TableQuery;


public class BasketService {

    private RelationalDatabase database;

    BasketService(RelationalDatabase database) {
        this.database = database;
    }

    public Boolean newItem(BasketData basketItem) {
        Boolean result = database.insert(basketItem);
        return result;
    }

    
    public BasketData updateItem(BasketData basketItem) {

        BasketData updatedBasketItem = (BasketData) database.update(basketItem);
        
        return updatedBasketItem;
    }
    
    public List<BasketData> loadBasket(Integer addressId) {

        Condition condition = new OR(new Equal("addressId", addressId));
                    
        TableQuery table = new TableQuery("basket")
                .select("addressId", "productId", "selectedQuantity")
                .where(condition);
                // .orderby("addressId");
        
        ArrayList<HashMap<String, Object>> results = database.select(table);

        List<BasketData> basket = convertResultsToObjects(results);
        
        return basket;
    }

    private List<BasketData> convertResultsToObjects(
                List<HashMap<String, Object>> resultSet){
        List<BasketData> resultsAsObjects = new ArrayList<>();
        Iterator<HashMap<String, Object>> itr = resultSet.iterator();

        Field[] fieldsArray = BasketData.class.getDeclaredFields();
        

        while(itr.hasNext()){
            HashMap<String, Object> row = itr.next();
            BasketData basketData = new BasketData();
            for (Field field: fieldsArray){
                if(field.getName() != "METADATA"){
                    try {
                        field.set(basketData, row.get(field.getName().toLowerCase()));;
                    } catch (IllegalAccessException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            resultsAsObjects.add(basketData);
        }

        return resultsAsObjects;
    }
    
    public Boolean delete(Object o) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Boolean select(Object o) {
        // TODO Auto-generated method stub
        return null;
    }

}