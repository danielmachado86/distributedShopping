package com.company.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.company.database.DatabaseConfiguration;
import com.company.database.jdbcConnectionProcessor;
import com.company.database.jdbcProcessorTemplate;
import com.company.database.jdbcStatementProcessor;
import com.company.resources.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import org.apache.commons.text.similarity.*;


public class Search {

    private static final int USELESS_KEYWORD_LENGTH = 2;

    private DatabaseConfiguration database;
    private Logger logger;

    public Search(DatabaseConfiguration database){
        this.database = database;

        logger = database.getLogger();
    }

    public List<SearchResult> go(String searchString) {
        List<String> arrayListOfValidKeywords = processSearchString(searchString);
        List<String> results = productQuery(arrayListOfValidKeywords);
        List<SearchResult> processedResults = processSearchResults(searchString, results);
        return processedResults;
    }

    public List<String> productQuery(List<String> keywords) {
        List<String> results = new ArrayList<String>();
        new jdbcProcessorTemplate().processConnection(database, new jdbcConnectionProcessor(){
            
            @Override
            public void processConnection(Connection connection) throws SQLException {
                String sql = buildQueryString(keywords);
                logger.newEntry("Generado SQL statement: " + sql);
                new jdbcProcessorTemplate().processStatement(sql, database, new jdbcStatementProcessor(){
                    
                    @Override
                    public void processStatement(ResultSet resultSet) throws SQLException {
                        while (resultSet.next()){
                            results.add(resultSet.getString(3));
                        }
                    }
                });
            }
        });
        return results;
    }

    private String buildQueryString(List<String> keywords) {
        Iterator<String> itr = keywords.iterator();
        String likeStatement = new String();
        while (itr.hasNext()) {
            String keyword = itr.next();
            String logicOp = "";
            if(itr.hasNext()){
                logicOp = " OR ";
            }
            likeStatement = likeStatement + "title ~~* " + "'%" + keyword + "%'" + logicOp;
        }
        String sql = "SELECT * FROM product WHERE " + likeStatement + ";";
        return sql;
    }

    private List<String> processSearchString(String searchString) {
        String lowerCaseSearchString = 
            searchString.toLowerCase();
        String arrayOfKeywords[] = 
            splitSearchStringBySpace(lowerCaseSearchString);
        List<String> arrayListOfKeywords = 
            convertStringArrayToArrayList(arrayOfKeywords);
        List<String> arrayListOfValidKeywords = 
            removeSmallKeywords(arrayListOfKeywords);
        return arrayListOfValidKeywords;
    }

    private List<SearchResult> processSearchResults(String searchString, List<String> results) {
        String result = null;
        List<SearchResult> unsortedList = new ArrayList<>();
        Iterator<String> itr = results.iterator();
        while(itr.hasNext()){
            result = itr.next();
            JaroWinklerDistance distanceContainer = 
            new JaroWinklerDistance();
            Double distanceIndex = 
            distanceContainer.apply(
                searchString.toLowerCase(), result.toLowerCase()
                );
            SearchResult sr = new SearchResult(result, distanceIndex);
            unsortedList.add(sr);
        }
            
        Collections.sort(unsortedList);
        Collections.reverse(unsortedList);
        Iterator<SearchResult> itr1 = unsortedList.iterator();
        SearchResult sr = null;
        String format = "%-48s%s%n";
        while(itr1.hasNext()){
            sr = itr1.next();
            System.out.printf(format, "Producto: " + sr.getTitle(), "Similaridad: " + sr.getSimilarity().toString());
        }
        return unsortedList;
    }

    private static String[] splitSearchStringBySpace(String searchString) {
        return searchString.split("[^a-zA-Z0-9á-ú']+", 0); 
    }

    private static List<String> convertStringArrayToArrayList(String[] stringArray) {
        return new ArrayList<String>(
            Arrays.asList(stringArray)
            );
    }

    private static List<String> removeSmallKeywords(List<String> arrayListOfKeywords){
        Iterator<String> itr = arrayListOfKeywords.iterator();
        while (itr.hasNext()) {
            String keyword = itr.next();
            if (keyword.length() <= USELESS_KEYWORD_LENGTH){
                itr.remove();
            }
        }
        return arrayListOfKeywords;

    }
    
}


class SearchResult implements Comparable<SearchResult>{

    private String title;
    private Double similarity;

    SearchResult(String title, Double similarity) {
        this.title = title;
        this.similarity = similarity;
    }

    @Override
    public int compareTo(SearchResult o) {
        return this.getSimilarity().compareTo(o.getSimilarity());
    }

    public Double setSimilarity(Double similarity) {
        return similarity;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public String getTitle() {
        return title;
    }
}