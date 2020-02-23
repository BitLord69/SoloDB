package com.janinc.query;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-21
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import java.util.*;

public class QueryResult {
    private String sortField;
    private List<HashMap<String, Object>> result = new ArrayList<>();

    public QueryResult(String sortField) {
        this.sortField = sortField;
    }

    public void addResultRecord(HashMap<String, Object> record) {
        result.add(record);
    } // addResultRecord

    public List<HashMap<String, Object>> getResults() { return sort(); } // getResults

    private List<HashMap<String, Object>> sort() {
        Comparator<HashMap<String, Object>> resultComparator = Comparator.comparing(this::getFieldValue);

        Collections.sort(result, resultComparator);
        return result;
    } // sort

    private <U extends Comparable<? super U>, T> U getFieldValue(T t) {
        Object value = ((HashMap)t).get(sortField);
        return (U) (value == null ? ""  : value);
    } // getFieldValue

    public int size() { return result.size(); } // size
    public boolean isEmpty() { return result.size()  == 0; } // isEmpty
} // class QueryResult
