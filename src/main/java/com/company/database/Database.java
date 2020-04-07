package com.company.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.company.resources.Logger;

public class Database {


    private String url;
    private String username;
    private String password;

    private Logger logger;
    
    public Database(String url, String username, String password){
        setURL(url);
        setUsername(username);
        setPassword(password);

    }

    public ArrayList<String> query(String sql) {
        ArrayList<String> results = new ArrayList<String>();
        jdbcProcessorTemplate jdbcProcessor = new jdbcProcessorTemplate(this);
        jdbcProcessor.setLogger(logger);
        jdbcProcessor.connection(new jdbcConnectionProcessor(){
            
            @Override
            public void connection() throws SQLException {
                logger.newEntry("Generado SQL statement: " + sql);
                jdbcProcessor.statement(sql, new jdbcStatementProcessor(){
                    
                    @Override
                    public void statement(ResultSet resultSet) throws SQLException {
                        while (resultSet.next()){
                            results.add(resultSet.getString(3));
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

    public String getURL() {
        return url;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setURL(String url) {
        this.url = url;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}