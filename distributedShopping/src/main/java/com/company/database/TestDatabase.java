package com.company.database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.sql.SQLException;

import com.company.resources.*;

import org.apache.ibatis.jdbc.ScriptRunner;

public class TestDatabase implements Database {

    private Connection conn = null;
    private Statement statement = null;
    private Logger log = new TestLogger();
    
    static final String DB_URL = "jdbc:h2:~/test";
    static final String DB_USER = "sa";
    static final String DB_PASSWORD = "";
    
    static final String TEST_DATA_FOLDER = "src/test/java/com/company/resources/";

    public Connection connect() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            log.newEntry("Base de datos conectada");
            statement = conn.createStatement();
            log.newEntry("Nuevo statement creado");
        } catch (final SQLException e) {
            log.newEntry("No se pudo realizar la conexion con la base de datos");
        }
        return conn;
    }

    public void initialize() {

        try {
            removeAllFromExistingDB();
            createTestObjects();
            populateTestData();
        } catch (final SQLException e) {
            log.newEntry(e.getMessage());
        } catch (final IOException e) {
            log.newEntry(e.getMessage());
        } finally {
            try {
                statement.close();
                log.newEntry("Se cerro statement");
            } catch (SQLException e) {
                log.newEntry(e.getMessage());
            }
            try {
                conn.close();
                log.newEntry("Se cerro conexion con la base de datos");
            } catch (SQLException e) {
                log.newEntry(e.getMessage());
            }
        }
    }

    private void createTestObjects() throws SQLException, IOException {

        String typeOfFileToFind = "sql" ;       

        FileFinder foundFiles = new FileFinder(TEST_DATA_FOLDER, typeOfFileToFind);
        List<Path> listOfFoundPaths = foundFiles.getListOfPaths();

        Iterator<Path> PathsIterator = listOfFoundPaths.iterator();
        Path path;
        
        ScriptRunner sr = new ScriptRunner(conn);
        log.newEntry("Nuevo Script Runner");
        //Creating a reader object
        
        while (PathsIterator.hasNext()) {
            path = PathsIterator.next();
            Reader reader = Files.newBufferedReader(path);
            log.newEntry("Nuevo Reader");
            sr.runScript(reader);
            log.newEntry("Tabla creada");
            reader.close();
            log.newEntry("BufferedReader cerrado");
        }
        
        
        
    }
    
    private void populateTestData() throws SQLException, IOException {
        
        String typeOfFileToFind = "sql" ;       
        
        FileFinder foundFiles = new FileFinder(TEST_DATA_FOLDER, typeOfFileToFind);
        List<Path> listOfFoundPaths = foundFiles.getListOfPaths();
    
        Iterator<Path> PathsIterator = listOfFoundPaths.iterator();
        Path path;

        while (PathsIterator.hasNext()) {
        
            BufferedReader lineReader = Files.newBufferedReader(PathsIterator.next());
            String lineText = null;
            
            lineReader.readLine(); // skip header line

            int batchSize = 20;
            
            String sql = "INSERT INTO product (brand_id, title) VALUES (?, ?)";

            PreparedStatement statement = conn.prepareStatement(sql);
            log.newEntry("Nuevo statement creado");
            
            int count = 0;
            
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String brand_id = data[0];
                String title = data[1];
                
                statement.setString(1, brand_id);
                statement.setString(2, title);
                
                statement.addBatch();
                
                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            lineReader.close();
            
            // execute the remaining queries
            statement.executeBatch();

            conn.commit();

            conn.rollback();
    }


        
    }

    private void removeAllFromExistingDB() throws SQLException {
        statement.execute("DROP ALL OBJECTS");
        log.newEntry("Se eliminaron todos los objetos de la base de datos");
        
    }
}