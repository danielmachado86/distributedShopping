package com.distributedShopping.database.SQL;


public class GT implements Condition {

    String condition;
    
    public GT(String field, Object value){

        this.condition = field + ">" + value;

    }

    @Override
    public String toString() {
        return condition;
    }

}