package com.company.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import java.nio.file.Files;
import java.nio.file.Path;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Iterator;
import java.util.List;

import com.company.resources.FileFinder;
import com.company.resources.TestLogger;

import org.apache.ibatis.jdbc.ScriptRunner;

public class TestEnvironment {
    
    private TestLogger testLogger;
    
    DatabaseConfiguration database;

    static final String TEST_DATA_FOLDER = "src/test/java/com/company/resources/";
    
    public TestEnvironment(DatabaseConfiguration database) { 

        this.database = database;

        testLogger = new TestLogger();
        database.setLogger(testLogger);
        initialize();
        
    }

    public void initialize() {
        new jdbcProcessorTemplate().processConnection(database, new jdbcConnectionProcessor(){
        
            @Override
            public void processConnection(Connection connection) throws SQLException {

                String sql = "DROP SCHEMA public CASCADE;";
                new jdbcProcessorTemplate().processCreateStatement(sql, database, new jdbcCreateStatementProcessor(){
                
                    @Override
                    public void processCreateStatement() throws SQLException {
                        testLogger.newEntry("Se eliminaron todos los objetos de la base de datos");
                    }
                });
                createTestObjects(connection);
                populateTestData(connection);
            }
        });
    }

    private void createTestObjects(Connection connection) {

        String typeOfFileToFind = "sql" ;       

        FileFinder foundFiles = new FileFinder(TEST_DATA_FOLDER, typeOfFileToFind);
        List<Path> listOfFoundPaths = foundFiles.getListOfPaths();

        Iterator<Path> PathsIterator = listOfFoundPaths.iterator();
        Path path;
        
        ScriptRunner sr = new ScriptRunner(connection);
        testLogger.newEntry("Nuevo Script Runner");
        
        while (PathsIterator.hasNext()) {
            path = PathsIterator.next();
            Reader reader = null;
            try {
                reader = Files.newBufferedReader(path);
                testLogger.newEntry("Nuevo Reader" + reader.toString());
                sr.runScript(reader);
                testLogger.newEntry("Tabla creada");
                
            } catch (IOException e) {
                testLogger.newEntry(e.getMessage());
            } finally {
                try {
                    reader.close();
                    testLogger.newEntry("BufferedReader cerrado");
                    
                } catch (IOException e) {
                    testLogger.newEntry(e.getMessage());
                }
                
            }

        }
        
        
        
    }
    
    private void populateTestData(Connection connection) throws SQLException {
        
        String typeOfFileToFind = "csv" ;       
        FileFinder foundFiles = new FileFinder(TEST_DATA_FOLDER, typeOfFileToFind);
        List<Path> listOfFoundPaths = foundFiles.getListOfPaths();
        Iterator<Path> PathsIterator = listOfFoundPaths.iterator();

        while (PathsIterator.hasNext()) {
            BufferedReader lineReader = null;
            try {
                lineReader = Files.newBufferedReader(PathsIterator.next());
                lineReader.readLine(); // skip header line
            } catch (IOException e) {
                testLogger.newEntry(e.getMessage());
            }
            int batchSize = 20;
            String sql = "INSERT INTO product (brand_id, title) VALUES (?, ?)";
            PreparedStatement statement = null;
            statement = connection.prepareStatement(sql);
            testLogger.newEntry("Nuevo statement creado para insertar nuevos registros en base de datos");
            
            try {
                String lineText = null;
                int count = 0;
                while ((lineText = lineReader.readLine()) != null) {
                    String[] data = lineText.split(",");
                    Integer brand_id = Integer.parseInt(data[0]);
                    String title = data[1];
                    statement.setInt(1, brand_id);
                    statement.setString(2, title);
                    statement.addBatch();
                    if (count % batchSize == 0) {
                        statement.executeBatch();
                    }
                }
            } catch (IOException e) {
                testLogger.newEntry(e.getMessage());
            } finally {
                try {
                    lineReader.close();
                } catch (IOException e) {
                    testLogger.newEntry(e.getMessage());
                }
            }
            statement.executeBatch();
            connection.commit();
            connection.rollback();
        }    
    }

}