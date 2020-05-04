package com.distributedShopping.database.SQL;


public class LT implements Condition {

    public String condition;

    public LT(String field, Object value){

        this.condition = field + "<" + value;
    }

    @Override
    public String toString() {
        return condition;
    }

}