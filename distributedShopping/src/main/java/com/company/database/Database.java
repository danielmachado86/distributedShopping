package com.company.database;

import java.sql.Connection;


interface Database {

    public Connection connect();
    public void initialize();

}