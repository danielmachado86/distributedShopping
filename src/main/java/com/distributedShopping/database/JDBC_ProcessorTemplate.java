package com.distributedShopping.database;

import java.sql.SQLException;
import java.sql.Statement;

import com.distributedShopping.resources.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBC_ProcessorTemplate {

    private Logger logger;
    private RelationalDatabase database;
    private Connection connection;

    public JDBC_ProcessorTemplate(RelationalDatabase database) {
        this.database = database;
        logger = database.getLogger();
	}

	public Connection connection(JDBC_ConnectionProcessor processor) {
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
    
    public void createStatement(String sql, JDBC_CreateObjectProcessor processor) {
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
    
    public void selectStatement(String sql, JDBC_SelectProcessor processor) {
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
    
    public void insertStatement(String sql, JDBC_InsertProcessor processor) {
        SQLException processSQLException = null;
        PreparedStatement statement = null;
        Integer result = 0;
        try {
            statement = connection.prepareStatement(sql);
            logger.newEntry("Nuevo Statement creado");
            result = statement.executeUpdate();
            logger.newEntry("Insert Statement ejecutado");
            processor.statement(result);
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