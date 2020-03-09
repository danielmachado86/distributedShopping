package com.company.resources;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CSVReader {

    private BufferedReader lineReader;
    private Logger log = new TestLogger();

    public CSVReader(String csvFilePath) {
        try {
            lineReader = new BufferedReader(new FileReader(csvFilePath));
            log.newEntry("Archivo " + csvFilePath + " cargado");
        } catch (FileNotFoundException e) {
            log.newEntry(e.getMessage());
        }
    }

    public BufferedReader read(){
        return lineReader;
    }

}