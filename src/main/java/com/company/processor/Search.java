package com.company.processor;

import java.util.ArrayList;
import java.util.List;

import com.company.database.Database;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import org.apache.commons.text.similarity.*;


public class Search {

    private static final int USELESS_KEYWORD_LENGTH = 2;

    private Database database;

    public Search(Database database){
        this.database = database;
    }

    public List<SearchResult> go(String searchString) {
        List<String> validKeywords = getValidKeywords(searchString);
        String sql = buildQueryString(validKeywords);
        List<String> results = database.query(sql);
        List<SearchResult> resultsAsObjects = getResultsAsObjects(results);
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
            if(itr.hasNext()){
                logicOp = " OR ";
            }
            likeStatement = likeStatement + "title ~~* " + "'%" + keyword + "%'" + logicOp;
        }
        String sql = "SELECT * FROM product WHERE " + likeStatement + ";";
        return sql;
    }

    private List<String> getValidKeywords(String searchString) {
        String lowerCaseSearchString = 
            searchString.toLowerCase();
        String keywords[] = 
            splitSearchStringBySpace(lowerCaseSearchString);
        List<String> arrayListOfKeywords = 
            convertStringArrayToArrayList(keywords);
        List<String> validKeywords = 
            removeSmallKeywords(arrayListOfKeywords);
        return validKeywords;
    }

    private List<SearchResult> getResultsAsObjects(List<String> results){
        String result = null;
        List<SearchResult> resultsAsObjects = new ArrayList<>();
        Iterator<String> itr = results.iterator();
        while(itr.hasNext()){
            result = itr.next();
            SearchResult sr = new SearchResult(result);
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
        Iterator<SearchResult> itr1 = results.iterator();
        SearchResult sr = null;
        String format = "%-48s%s%n";
        while(itr1.hasNext()){
            sr = itr1.next();
            System.out.printf(format, "Producto: " + sr.getTitle(), "Similaridad: " + sr.getSimilarity().toString());
        }
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

    private String title;
    private Double similarity = null;

    SearchResult(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(SearchResult o) {
        return this.getSimilarity().compareTo(o.getSimilarity());
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public String getTitle() {
        return title;
    }
}