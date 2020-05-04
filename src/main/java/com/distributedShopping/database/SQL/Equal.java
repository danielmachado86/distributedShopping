package com.distributedShopping.database.SQL;


public class Equal implements Condition{

    public String condition;

    public Equal(String field, Object value){

        this.condition = field + "=" + value;

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return condition;
    }

}