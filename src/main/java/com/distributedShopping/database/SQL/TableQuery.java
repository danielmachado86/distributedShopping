package com.distributedShopping.database.SQL;

import java.util.ArrayList;
import java.util.List;

public class TableQuery {

    public String tableName;
    public List<String> select = new ArrayList<String>();
    public Object whereConditions;
    public String oderConditions;

    public TableQuery(String tableName){

        this.tableName = tableName;

    }

    public TableQuery select(){
        return this;
    }
    
    public TableQuery select(String ...a){
        for(String i: a){
            select.add(i);
        }
        return this;
    }
    
    public TableQuery where(Object condition){

        whereConditions = condition;
        
        return this;
    }
    
    public TableQuery orderby(String condition){

        oderConditions = condition;
        
        return this;
    }

}