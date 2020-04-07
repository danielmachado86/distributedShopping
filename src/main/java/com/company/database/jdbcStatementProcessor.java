package com.company.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface jdbcStatementProcessor {

public void statement(ResultSet resultSet) throws SQLException;

}