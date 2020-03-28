package com.company.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface jdbcConnectionProcessor {

    public void processConnection(Connection connection) throws SQLException;

}