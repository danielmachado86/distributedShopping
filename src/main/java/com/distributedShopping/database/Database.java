package com.distributedShopping.database;

import java.util.ArrayList;
import java.util.HashMap;

import com.distributedShopping.database.SQL.TableQuery;

public interface Database {

    public Boolean insert(Object o);
    public Object update(Object o);
    public Boolean delete(Object o);
    public ArrayList<HashMap<String, Object>> select(TableQuery tableQuery);


}