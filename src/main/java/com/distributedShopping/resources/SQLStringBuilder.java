package com.distributedShopping.resources;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.distributedShopping.database.SQL.TableQuery;

public class SQLStringBuilder {

    private Metadata metadata;
    private Map<String, Object> fields;

    public SQLStringBuilder(Object o){
        getListOfFields(o);
    }

    public void getListOfFields(Object o) {

        Class<?> cl = o.getClass();
        fields = new HashMap<String, Object>();
        metadata = new Metadata();


        Object value = null;

        for(Field field : cl.getDeclaredFields()) {
            try {
                value = field.get(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(true);
            if (value instanceof Metadata){
                metadata = (Metadata) value;
            } else{
                fields.put(field.getName(), value);
            }
        }
    }
    
    public String insert() {
                
        Iterator<Map.Entry<String, Object>> itr = fields.entrySet().iterator();
        
        StringBuilder fieldsString = new StringBuilder();
        StringBuilder valuesString = new StringBuilder();
        int count = 0;        
        while(itr.hasNext()){
            Entry<String, Object> entry = itr.next();
            
            String field = entry.getKey();
            String value = String.valueOf(entry.getValue());
            
            if(count==0){
                fieldsString.append(field);
                valuesString.append(value);

            } else {
                fieldsString.append(", " + field);
                valuesString.append(", " + value);
            }
            
            count++;
        }

        StringBuilder sqlBuilder = new StringBuilder();
        
        sqlBuilder.append("INSERT INTO ");
        sqlBuilder.append(metadata.tableName);
        sqlBuilder.append(" ");
        sqlBuilder.append("(");
        sqlBuilder.append(fieldsString);
        sqlBuilder.append(") ");
        sqlBuilder.append("VALUES ");
        sqlBuilder.append("(");
        sqlBuilder.append(valuesString);
        sqlBuilder.append(");");

        String sql = sqlBuilder.toString();
        
        return sql;
    }
    
    public String update() {
        
        Iterator<Map.Entry<String, Object>> itr = fields.entrySet().iterator();
        
        StringBuilder fieldsString = new StringBuilder();
        StringBuilder setExpr = new StringBuilder();
        
        int count = 0;
        while(itr.hasNext()){
            Entry<String, Object> entry = itr.next();
            String field = entry.getKey();
            String value = String.valueOf(entry.getValue());
            
            if(count==0){
                fieldsString.append(field);
                setExpr.append(field + "=" + value);
                
            } else {
                fieldsString.append(", " + field);
                setExpr.append(", " + field + "=" + value);
                System.out.println(setExpr);
            }
            
            count++;
        }
        
        StringBuilder whereExpr = new StringBuilder();;
        for(int i=0;i<metadata.primaryKey.length;i++){
            
            String field = metadata.primaryKey[i];
            Integer value = (Integer) fields.get(field);
            
            if(i==0){
                whereExpr.append(field + "=" + value);

            } else {

                whereExpr.append(" AND " + field + "=" + value);
            }
            
        }
        

        StringBuilder sqlBuilder = new StringBuilder();
        
        sqlBuilder.append("UPDATE ");
        sqlBuilder.append(metadata.tableName);
        sqlBuilder.append(" SET ");
        sqlBuilder.append(setExpr);
        sqlBuilder.append(" WHERE ");
        sqlBuilder.append(whereExpr);
        sqlBuilder.append(" RETURNING ");
        sqlBuilder.append(fieldsString);
        sqlBuilder.append(";");
        
        String sql = sqlBuilder.toString();
        
        return sql;
    }
    
    public static String select(TableQuery table){

        Object condition = table.whereConditions;
        
        StringBuilder sqlBuilder = new StringBuilder();
        
        sqlBuilder.append("SELECT ");
        sqlBuilder.append(table.select);
        sqlBuilder.append(" FROM ");
        sqlBuilder.append(table.tableName);
        sqlBuilder.append(" WHERE ");
        sqlBuilder.append(condition);
        sqlBuilder.append(";");
        
        String sql = sqlBuilder.toString();
        sql = sql.replaceAll("[\\[\\]]+", "");

        return sql;
    }
}