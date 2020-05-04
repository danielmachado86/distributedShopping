package com.distributedShopping.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface JDBC_SelectProcessor {

public void statement(ResultSet resultSet) throws SQLException;

}