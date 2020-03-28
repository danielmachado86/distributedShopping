package com.company.database;

import java.sql.Connection;

import com.company.resources.Logger;

public class DatabaseConfiguration {

    private Connection connection;

    private String url;
    private String username;
    private String password;

    private Logger logger;
    
    public DatabaseConfiguration(String url, String username, String password){
        setURL(url);
        setUsername(username);
        setPassword(password);

    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public Connection getConnection(){
        return this.connection;
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