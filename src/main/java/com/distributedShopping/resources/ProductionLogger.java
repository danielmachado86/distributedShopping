package com.distributedShopping.resources;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProductionLogger implements Logger{

    private String logType;


    public ProductionLogger() {
        setLogType("App");
    }
    
    public void newEntry(String message) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(now.format(formatter) + ": " + this.logType + ": " + message);
    }

    private void setLogType(String logType){
        this.logType = logType;
    }
}