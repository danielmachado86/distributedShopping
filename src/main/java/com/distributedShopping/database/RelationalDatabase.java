package com.distributedShopping.database;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.distributedShopping.database.SQL.TableQuery;
import com.distributedShopping.resources.Logger;
import com.distributedShopping.resources.ProductionLogger;
import com.distributedShopping.resources.SQLStringBuilder;

public class RelationalDatabase implements Database{


    private String url;
    private String username;
    private String password;

    private JDBC_ProcessorTemplate jdbcProcessor;
    private Logger logger = new ProductionLogger();
    
    public RelationalDatabase(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    @Override
    public ArrayList<HashMap<String, Object>> select(TableQuery tableQuery) {
        String sql = SQLStringBuilder.select(tableQuery);
        System.out.println(sql);
        ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();
        jdbcProcessor.connection(new JDBC_ConnectionProcessor(){
            @Override
            public void connection() throws SQLException {
                jdbcProcessor.selectStatement(sql, new JDBC_SelectProcessor(){
                    @Override
                    public void statement(ResultSet resultSet) throws SQLException {
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int columns = metaData.getColumnCount();
                        while (resultSet.next()){
                            HashMap<String, Object> row = new HashMap<String, Object>(columns);
                            for(int i=1; i<=columns; ++i){           
                                row.put(metaData.getColumnName(i),resultSet.getObject(i));
                            }
                            results.add(row);
                        }

                    }
                });
            }
        });
        return results;
    }
    
    @Override
    public Boolean insert(Object o) {
        String sql = buildInsertQueryString(o);
        Integer result[] = {0};
        jdbcProcessor.connection(new JDBC_ConnectionProcessor(){
            @Override
            public void connection() throws SQLException {
                jdbcProcessor.insertStatement(sql, new JDBC_InsertProcessor(){
                    @Override
                    public void statement(Integer response) throws SQLException {
                        result[0] = response;
                        
                    }
                });
            }
        });
        Boolean success = false;
        if (result[0] == 1){
            success = true;
        }
        return success;
    }
    
    private String buildInsertQueryString(Object o) {
        SQLStringBuilder sqlStringBuilder = new SQLStringBuilder(o);
        String sql = sqlStringBuilder.insert();
        return sql;
    }
    
    @Override
    public Object update(Object o) {

        String sql = buildUpdateQueryString(o);
        System.out.println(sql);
        HashMap<String, Object> result = new HashMap<String, Object>();
        jdbcProcessor.connection(new JDBC_ConnectionProcessor(){
            @Override
            public void connection() throws SQLException {
                jdbcProcessor.selectStatement(sql, new JDBC_SelectProcessor(){
                    @Override
                    public void statement(ResultSet response) throws SQLException {
                        ResultSetMetaData metaData = response.getMetaData();
                        int columns = metaData.getColumnCount();
                        while (response.next()){
                            for(int i=1; i<=columns; ++i){           
                                result.put(metaData.getColumnName(i),response.getObject(i));
                                try {
                                    Field field = o.getClass().getDeclaredField(metaData.getColumnName(i));
                                    field.set(this, response.getObject(i));
                                    
                                } catch (NoSuchFieldException | IllegalAccessException e) {
                                    logger.newEntry(e.getMessage());
                                }
                            }
                        }

                    }
                });
            }
        });
        return o;
    }

    private static String buildUpdateQueryString(Object o) {
        SQLStringBuilder sqlStringBuilder = new SQLStringBuilder(o);
        String sql = sqlStringBuilder.update();
        return sql;
    }
    
    @Override
    public Boolean delete(Object o) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setLogger(Logger logger){
        this.logger = logger;
    }
    
    public Logger getLogger(){
        return this.logger;
    }
    
    public JDBC_ProcessorTemplate getJdbcProcessor(){
        return this.jdbcProcessor;
    }

    public String getURL() {
        return url;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
	public void setJdbcProcessor(JDBC_ProcessorTemplate jdbcProcessor) {
        this.jdbcProcessor = jdbcProcessor;
    }
    
	public void newJdbcProcessor(JDBC_ProcessorTemplate jdbcProcessor) {
        this.jdbcProcessor = new JDBC_ProcessorTemplate(this);
	}

}