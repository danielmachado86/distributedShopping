package com.distributedShopping.processor;


import com.distributedShopping.resources.Metadata;

public class BasketData{

    public Integer addressId;
    public Integer productId;
    public Double selectedQuantity;
    public static final Metadata METADATA = setMetadata();


    private static final Metadata setMetadata() {

        Metadata metadata = new Metadata();
        
        metadata.tableName = "basket";
        metadata.primaryKey = new String[] {"addressId", "productId"};
        
        return METADATA;
    }
    
    
    public BasketData(){

    }

    public BasketData(Integer addressId, Integer productId, Double quantity){

        this.addressId = addressId;
        this.productId = productId;
        this.selectedQuantity = quantity;

    }
}