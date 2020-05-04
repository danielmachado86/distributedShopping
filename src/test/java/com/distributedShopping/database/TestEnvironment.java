package com.distributedShopping.database;

import java.io.IOException;
import java.io.Reader;

import java.nio.file.Files;
import java.nio.file.Path;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import com.distributedShopping.resources.FileFinder;
import com.distributedShopping.resources.TestLogger;

import org.apache.ibatis.jdbc.ScriptRunner;

public class TestEnvironment {
    
    private TestLogger testLogger = new TestLogger();
    private JDBC_ProcessorTemplate jdbcProcessor;
    static final String TEST_DATA_FOLDER = "src/test/java/com/distributedShopping/resources/";
    
    public TestEnvironment(RelationalDatabase database) { 
        database.setLogger(testLogger);
        jdbcProcessor = new JDBC_ProcessorTemplate(database);
        database.setJdbcProcessor(jdbcProcessor);
        destroy();
        createTestObjects();
        
    }

    public void destroy() {
        jdbcProcessor.connection(new JDBC_ConnectionProcessor(){
            @Override
            public void connection() throws SQLException {

                String sql = "DROP SCHEMA public CASCADE;";
                jdbcProcessor.createStatement(sql, new JDBC_CreateObjectProcessor(){
                
                    @Override
                    public void createStatement() throws SQLException {
                        testLogger.newEntry("Se eliminaron todos los objetos de la base de datos");
                    }
                });
            }
        });
    }

    private void createTestObjects() {

        jdbcProcessor.connection(new JDBC_ConnectionProcessor(){
            @Override
            public void connection() throws SQLException {
                String typeOfFileToFind = "sql" ;       

                FileFinder foundFiles = new FileFinder(TEST_DATA_FOLDER, typeOfFileToFind);
                List<Path> listOfFoundPaths = foundFiles.getListOfPaths();

                Iterator<Path> PathsIterator = listOfFoundPaths.iterator();
                Path path;
                
                ScriptRunner sr = new ScriptRunner(jdbcProcessor.getConnection());
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
        });
    
    }

}