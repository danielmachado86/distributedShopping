package com.distributedShopping.processor;

public class ProductData {

    public Integer id;
    public Integer brand;
    public String title;
    public Double similarity = null;

    ProductData(Integer id, Integer brand, String title) {
        this.id = id;
        this.brand = brand;
        this.title = title;
    }

}