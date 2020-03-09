package com.company.resources;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private String logType;


    public Logger() {
        setLogType("App");
    }

    public void newEntry(String message) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(now.format(formatter) + ": " + this.logType + ": " + message);
    }
    
    public void newEntry(Path path) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(now.format(formatter) + ": " + this.logType + ": Se encontro el archivo " + path);
    }

    public void setLogType(String logType){
        this.logType = logType;
    }
}