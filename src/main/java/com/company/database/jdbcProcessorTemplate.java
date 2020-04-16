package com.company.database;

import java.sql.SQLException;
import java.sql.Statement;

import com.company.resources.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class jdbcProcessorTemplate {

    private Logger logger;
    private Database database;
    private Connection connection;

    public jdbcProcessorTemplate(Database database) {
        this.database = database;
        logger = database.getLogger();
	}

	public Connection connection(jdbcConnectionProcessor processor) {
        SQLException processSQLException = null;
        connection = null;
        try {
            connection = DriverManager.getConnection(
                database.getURL(),
                database.getUsername(),
                database.getPassword()
            );
            logger.newEntry("Base de datos conectada");
            processor.connection();
        } catch (SQLException e) {
            processSQLException = e;
        } finally {
            if (connection != null){
                try {
                    connection.close();
                    logger.newEntry("Conexion con Base de datos ha sido cerrada");
                } catch (SQLException e) {
                    if (processSQLException != null){
                        logger.newEntry(processSQLException.getMessage());
                    }else{
                        logger.newEntry(e.getMessage());
                    }
                }
            }
            if (processSQLException != null){
                logger.newEntry(processSQLException.getMessage());
            }
        }
        return connection;
    }
    
    public void createStatement(String sql, jdbcCreateStatementProcessor processor) {
        SQLException processSQLException = null;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            logger.newEntry("Nuevo Statement creado");
            statement.execute(sql);
            logger.newEntry("Statement ejecutado");
            processor.createStatement();
        } catch (SQLException e) {
            processSQLException = e;
        } finally {
            if (processSQLException != null){
                logger.newEntry(processSQLException.getMessage());
            }
            if (connection != null){
                try {
                    statement.close();
                    logger.newEntry("Statement ha sido cerrado");
                } catch (SQLException e) {
                    if (processSQLException != null){
                        logger.newEntry(processSQLException.getMessage());
                    }else{
                        logger.newEntry(e.getMessage());
                    }
                }
            }
        }
    } 
    
    public void statement(String sql, jdbcStatementProcessor processor) {
        SQLException processSQLException = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            logger.newEntry("Nuevo Statement creado");
            resultSet = statement.executeQuery();
            logger.newEntry("Query Statement ejecutado");
            processor.statement(resultSet);
        } catch (SQLException e) {
            processSQLException = e;
        } finally {
            if (processSQLException != null){
                logger.newEntry(processSQLException.getMessage());
            }
            if (connection != null){
                try {
                    statement.close();
                    logger.newEntry("Statement ha sido cerrado");
                } catch (SQLException e) {
                    if (processSQLException != null){
                        logger.newEntry(processSQLException.getMessage());
                    }else{
                        logger.newEntry(e.getMessage());
                    }
                }
            }
        }
    }

    public Connection getConnection() {
        return connection;
    } 
}