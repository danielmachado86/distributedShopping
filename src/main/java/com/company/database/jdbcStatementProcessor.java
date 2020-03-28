package com.company.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface jdbcStatementProcessor {

public void processStatement(ResultSet resultSet) throws SQLException;

}