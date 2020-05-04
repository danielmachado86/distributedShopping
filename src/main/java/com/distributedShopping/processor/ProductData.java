package com.distributedShopping.processor;


public class ProductData {

    public Integer id;
    public Integer brandid;
    public String title;

    ProductData() {
    }

    ProductData(Integer id, Integer brand, String title) {
        this.id = id;
        this.brandid = brand;
        this.title = title;
    }

}