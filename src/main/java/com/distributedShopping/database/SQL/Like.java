package com.distributedShopping.database.SQL;

public class Like {

    public String condition;

    public Like(String field, Object value){

        this.condition = field + "~~*" + "'%" + value + "%'";

    }

    @Override
    public String toString() {
        return condition;
    }


}