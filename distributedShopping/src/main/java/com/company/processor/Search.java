package com.company.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.commons.text.similarity.*;

public class Search {

    private static final int USELESS_KEYWORD_LENGTH = 2;

    public static void go(String searchString) {
        List<String> arrayListOfValidKeywords = processSearchString(searchString);
    }

    private static List<String> processSearchString(String searchString) {
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

    private static Double processSearchResults(String searchString, String rightText) {
        JaroWinklerDistance distanceContainer = 
            new JaroWinklerDistance();
        Double distanceIndex = 
            distanceContainer.apply(
                searchString.toLowerCase(), rightText.toLowerCase()
                );
        return distanceIndex;
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