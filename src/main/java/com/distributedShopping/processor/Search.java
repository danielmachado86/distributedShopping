package com.distributedShopping.processor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.distributedShopping.database.Database;
import com.distributedShopping.database.SQL.Condition;
import com.distributedShopping.database.SQL.Like;
import com.distributedShopping.database.SQL.OR;
import com.distributedShopping.database.SQL.TableQuery;
import com.distributedShopping.resources.Logger;
import com.distributedShopping.resources.ProductionLogger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.text.similarity.*;

public class Search {

    private static final int USELESS_KEYWORD_LENGTH = 2;

    private Database database;
    private Logger logger = new ProductionLogger();

    public Search(Database database) {
        this.database = database;
    }

    public List<SearchResult> go(String searchString) throws SQLException {
        String[] validKeywords = getValidKeywords(searchString);
        TableQuery query = buildQuery(validKeywords);
        List<HashMap<String, Object>> dbResults = database.select(query);
        List<SearchResult> resultsAsObjects = convertResultsToObjects(dbResults);
        List<SearchResult> processedResults = calculateStringSimilarity(searchString, resultsAsObjects);
        List<SearchResult> sortedResults = sortResults(processedResults);
        return sortedResults;
    }
    

    private String[] getValidKeywords(String searchString) {
        String lowerCaseSearchString = searchString.toLowerCase();
        String[] keywords = splitSearchStringBySpace(lowerCaseSearchString);
        String[] validKeywords = removeSmallKeywords(keywords);
        return validKeywords;
    }
    
    private static String[] splitSearchStringBySpace(String searchString) {
        return searchString.split("[^a-zA-Z0-9á-ú']+", 0); 
    }

    
    private static String[] removeSmallKeywords(String[] keywords){
        List<String> validKeywordsList = new ArrayList<String>();
        for (String keyword: keywords) {
            if (keyword.length() >= USELESS_KEYWORD_LENGTH){
                validKeywordsList.add(keyword);
            }
        }
        String[] validKeywords = new String[validKeywordsList.size()];
        validKeywordsList.toArray(validKeywords);

        return validKeywords;

    }


    private TableQuery buildQuery(String[] keywords) {

        Like[] conditionList = new Like[keywords.length];
        for(int i=0; i<keywords.length; i++){
            conditionList[i] = new Like("title", keywords[i]);
        }

        Condition condition = new OR((Object[]) conditionList);
        TableQuery tableQuery = new TableQuery("product")
                .select("*")
                .where(condition);

        return tableQuery;
    }


    private List<SearchResult> convertResultsToObjects(
                    List<HashMap<String, Object>> resultSet){
        List<SearchResult> resultsAsObjects = new ArrayList<>();
        Iterator<HashMap<String, Object>> itr = resultSet.iterator();
        while(itr.hasNext()){
            HashMap<String, Object> row = itr.next();
            SearchResult sr = new SearchResult(
                    (Integer) row.get("id"),
                    (Integer) row.get("brandid"),
                    (String) row.get("title")
            );
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
            title = result.product.title;
            JaroWinklerDistance distanceContainer = new JaroWinklerDistance();
            Double distanceIndex = 
            distanceContainer.apply(
                searchString.toLowerCase(), title.toLowerCase()
            );
            result.similarity = distanceIndex;
            System.out.println(result.product.title + "\t" + result.similarity);
        }
            
        return results;
    }

    
    private List<SearchResult> sortResults(List<SearchResult> results) {
        Collections.sort(results);
        Collections.reverse(results);
        return results;
    }
    
}


class SearchResult implements Comparable<SearchResult>{

    public ProductData product;
    public Double similarity = 0.0;

    SearchResult(Integer id, Integer brand, String title) {
        product = new ProductData(id, brand, title);
    }

    @Override
    public int compareTo(SearchResult o) {
        return similarity.compareTo(o.similarity);
    }
    

}