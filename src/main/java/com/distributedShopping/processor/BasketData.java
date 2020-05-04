package com.distributedShopping.processor;

import java.util.ArrayList;
import java.util.Arrays;

import com.distributedShopping.resources.Metadata;

public class BasketData{

    public Integer addressId;
    public Integer productId;
    public Double selectedQuantity;
    public Metadata metadata = new Metadata();

    public BasketData(Integer addressId, Integer productId, Double quantity){

        metadata.tableName = "basket";
        metadata.primaryKey = new String[] {"addressId", "productId"};

        this.addressId = addressId;
        this.productId = productId;
        this.selectedQuantity = quantity;

    }
}