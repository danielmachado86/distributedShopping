package com.distributedShopping.processor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.distributedShopping.database.Database;
import com.distributedShopping.resources.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.text.similarity.*;

public class Search {

    private static final int USELESS_KEYWORD_LENGTH = 2;

    private Database database;
    private Logger logger;

    public Search(Database database) {
        this.database = database;
        logger = database.getLogger();
    }

    public List<SearchResult> go(String searchString) throws SQLException {
        List<String> validKeywords = getValidKeywords(searchString);
        String sqlString = buildQueryString(validKeywords);
        List<HashMap<String, Object>> dbResults = database.query(sqlString);
        List<SearchResult> resultsAsObjects = convertResultsToObjects(dbResults);
        List<SearchResult> processedResults = calculateStringSimilarity(searchString, resultsAsObjects);
        List<SearchResult> sortedResults = sortResults(processedResults);
        return sortedResults;
    }

    private String buildQueryString(List<String> keywords) {
        Iterator<String> itr = keywords.iterator();
        String likeStatement = new String();
        while (itr.hasNext()) {
            String keyword = itr.next();
            String logicOp = "";
            if (itr.hasNext()) {
                logicOp = " OR ";
            }
            likeStatement = likeStatement + "title ~~* " + "'%" + keyword + "%'" + logicOp;
        }
        String sql = "SELECT * FROM product WHERE " + likeStatement + ";";
        logger.newEntry("SQL statement: " + sql);
        return sql;
    }

    private List<String> getValidKeywords(String searchString) {
        String lowerCaseSearchString = searchString.toLowerCase();
        String keywords[] = splitSearchStringBySpace(lowerCaseSearchString);
        List<String> arrayListOfKeywords = convertStringArrayToArrayList(keywords);
        List<String> validKeywords = removeSmallKeywords(arrayListOfKeywords);
        return validKeywords;
    }

    private List<SearchResult> convertResultsToObjects(List<HashMap<String, Object>> resultSet){
        List<SearchResult> resultsAsObjects = new ArrayList<>();
        Iterator<HashMap<String, Object>> itr = resultSet.iterator();
        while(itr.hasNext()){
            HashMap<String, Object> row = itr.next();
            SearchResult sr = new SearchResult((String) row.get("title"));
            resultsAsObjects.add(sr);
        }
            
        return resultsAsObjects;
    }

    private List<SearchResult> calculateStringSimilarity(String searchString, List<SearchResult> results) {
        SearchResult result = null;
        String title = null;
        Iterator<SearchResult> itr = results.iterator();
        while(itr.hasNext()){
            result = itr.next();
            title = result.getTitle();
            JaroWinklerDistance distanceContainer = new JaroWinklerDistance();
            Double distanceIndex = 
            distanceContainer.apply(
                searchString.toLowerCase(), title.toLowerCase()
            );
            result.setSimilarity(distanceIndex);
        }
            
        return results;
    }

    private List<SearchResult> sortResults(List<SearchResult> results) {
        Collections.sort(results);
        Collections.reverse(results);
        return results;
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

    private Integer id;
    private Integer brand;
    private String title;
    private Double similarity = null;

    SearchResult(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(SearchResult o) {
        return this.getSimilarity().compareTo(o.getSimilarity());
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public void setBrand(Integer brand) {
        this.brand = brand;
    }
    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public Integer getId() {
        return id;
    }
    public Integer getBrand() {
        return brand;
    }
    public String getTitle() {
        return title;
    }
    public Double getSimilarity() {
        return similarity;
    }
}