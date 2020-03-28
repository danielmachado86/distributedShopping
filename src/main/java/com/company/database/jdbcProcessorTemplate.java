package com.company.database;

import java.sql.SQLException;
import java.sql.Statement;

import com.company.resources.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class jdbcProcessorTemplate {

    public void processConnection(DatabaseConfiguration database, jdbcConnectionProcessor processor) {
        SQLException processSQLException = null;
        Connection connection = null;
        Logger logger = database.getLogger();
        try {
            connection = DriverManager.getConnection(database.getURL(), database.getUsername(), database.getPassword());
            database.setConnection(connection);
            logger.newEntry("Base de datos conectada");
            processor.processConnection(connection);
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
    }
    
    public void processCreateStatement(String sql, DatabaseConfiguration database, jdbcCreateStatementProcessor processor) {
        SQLException processSQLException = null;
        Statement statement = null;
        Connection connection = database.getConnection();
        Logger logger = database.getLogger();
        try {
            statement = connection.createStatement();
            logger.newEntry("Nuevo Statement creado");
            statement.execute(sql);
            logger.newEntry("Statement ejecutado");
            processor.processCreateStatement();
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
    
    public void processStatement(String sql, DatabaseConfiguration database, jdbcStatementProcessor processor) {
        SQLException processSQLException = null;
        PreparedStatement statement = null;
        Connection connection = database.getConnection();
        Logger logger = database.getLogger();
        try {
            statement = connection.prepareStatement(sql);
            logger.newEntry("Nuevo Statement creado");
            ResultSet resultSet = statement.executeQuery();
            logger.newEntry("Query Statement ejecutado");
            processor.processStatement(resultSet);
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
}