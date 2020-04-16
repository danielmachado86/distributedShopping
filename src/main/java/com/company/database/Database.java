package com.company.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.company.resources.Logger;
import com.company.resources.ProductionLogger;

public class Database {


    private String url;
    private String username;
    private String password;

    private jdbcProcessorTemplate jdbcProcessor;
    private Logger logger = new ProductionLogger();
    
    public Database(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    public ArrayList<HashMap<String, Object>> query(String sql) {
        ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();
        jdbcProcessor.connection(new jdbcConnectionProcessor(){
            @Override
            public void connection() throws SQLException {
                jdbcProcessor.statement(sql, new jdbcStatementProcessor(){
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

    public void setLogger(Logger logger){
        this.logger = logger;
    }
    
    public Logger getLogger(){
        return this.logger;
    }
    
    public jdbcProcessorTemplate getJdbcProcessor(){
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
    
	public void setJdbcProcessor(jdbcProcessorTemplate jdbcProcessor) {
        this.jdbcProcessor = jdbcProcessor;
    }
    
	public void newJdbcProcessor(jdbcProcessorTemplate jdbcProcessor) {
        this.jdbcProcessor = new jdbcProcessorTemplate(this);
	}
}