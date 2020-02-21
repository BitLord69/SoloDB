package com.janinc.query;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.*;
import com.janinc.exceptions.*;
import com.janinc.field.FieldManager;
import com.janinc.query.clause.*;
import com.janinc.util.Debug;
import com.janinc.util.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Query {
    private String fromTable = "";
    private String sortField = "";
    private List<String> fieldsToRetrieve = new ArrayList<>();
    private List<WhereClause> clauses = new ArrayList<>();
    private BindingOperator bindingOperation = BindingOperator.AND;

    public Query select(String... fields) {
        this.fieldsToRetrieve.addAll(Arrays.asList(fields));
        return this;
    } // select

    public Query sort(String sortField) {
        this.sortField = sortField;
        return this;
    } // sort

    public Query from(String table) {
        if (!Database.getInstance().tableExists(table)) {
            throw new TableNotFoundException(table);
        } // if !Database...

        fromTable = table;
        return this;
    } // from

    public Query from(Class<? extends DataObject> dataClass)  {
        Database db = Database.getInstance();
        if (!db.tableExists(dataClass)) {
            throw new TableNotFoundException(dataClass.toString());
        } // if !Database...

        fromTable = db.getTableName(dataClass);
        return this;
    } // from

    public Query bindingOperator(BindingOperator op) {
        bindingOperation = op;
        return this;
    } // bindingOperator

    public Query where(WhereClause clause) {
        clauses.add(clause);
        return this;
    } // where

    public Query where(String field, Operator operator, String comparator) {
        clauses.add(new StringClause(field, operator, comparator));
        return this;
    } // where

    public Query where(String field, String operator, String comparator) {
        clauses.add(new StringClause(field, operator, comparator));
        return this;
    } // where

    public Query where(String field, Operator operator, int comparator) {
        clauses.add(new IntClause(field, operator, comparator));
        return this;
    } // where

    public Query where(String field, String operator, int comparator) {
        clauses.add(new IntClause(field, operator, comparator));
        return this;
    } // where

    public Query where(String field, Operator operator, float comparator) {
        clauses.add(new FloatClause(field, operator, comparator));
        return this;
    } // where

    public Query where(String field, String operator, float comparator) {
        clauses.add(new FloatClause(field, operator, comparator));
        return this;
    } // where

    public Query where(String field, Operator operator, boolean comparator) {
        clauses.add(new BooleanClause(field, operator, comparator));
        return this;
    } // where

    public Query where(String field, String operator, boolean comparator) {
        clauses.add(new BooleanClause(field, operator, comparator));
        return this;
    } // where

    private void checkQueryParameters() throws QueryException {
        if (fromTable.equals("")) throw new QueryException("From table has not been set!");
        if (fieldsToRetrieve.size() == 0) throw new QueryException("No call to select() has been made!");
        if (clauses.size() == 0) throw new QueryException("No Where-clauses present!");

        if (sortField.equals("")) {
            sortField = fieldsToRetrieve.get(0);
        } else {
            List<String> l = new ArrayList<>();
            l.add(sortField);
            checkFields(l);
        } // else

        if (fieldsToRetrieve.size() == 1 && fieldsToRetrieve.get(0).equals("*")) {
            addAllFields();
           } // if fieldsToRetrieve...
        else {
            checkFields(fieldsToRetrieve);
        } // else

        checkFields(clauses.stream().map(WhereClause::getFieldName).collect(Collectors.toList()));
    } // checkQueryParameters

    private boolean checkSearchFieldUniqueness() {
        FieldManager fm = Database.getInstance().getFieldManager(fromTable);
        long numUniques = clauses.stream().filter(wc -> fm.isFieldUnique(wc.getFieldName()) && wc.getOperator() == Operator.EQUALS).count();
        if (clauses.size() == 1)
            return numUniques == 1;
        else
            return (bindingOperation == BindingOperator.AND && numUniques == clauses.size());
    } // checkSearchFieldUniqueness

    public QueryResult execute() throws QueryException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//    public ArrayList<HashMap<String, Object>> execute() throws QueryException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        checkQueryParameters();

        if (Debug.ON) System.out.println("In Query.execute - all fields checked OK!");

        Database db = Database.getInstance();
        QueryResult result = new QueryResult(sortField);
//        ArrayList<HashMap<String, Object>> result = new ArrayList<>();
        Iterator<? extends Map.Entry<String, ? extends DataObject>> i = db.getIterator(fromTable);

        // TODO: 2020-02-18 Check if we are only using the id-field (or an index field when/if those are implemented), since then we can ask the hashmap for the value without the need to do a proper search

        boolean allSearchFieldsAreUnique = checkSearchFieldUniqueness();
//        allSearchFieldsAreUnique = false; // Uncomment this line to disable SSE (search shortcut evaluation)

        while (i.hasNext()) {
            boolean totResult;
            DataObject d = (i.next()).getValue();
            ArrayList<Boolean> partialResults = new ArrayList<>();

            for (WhereClause c : clauses) {
                partialResults.add(c.compare(fromTable, d));
            } // for c...

            totResult = partialResults.get(0);
            if (partialResults.size() > 1) {
                // for j...
                for (int j = 1; j < partialResults.size(); j++)
                    totResult = bindingOperation.function.test(totResult, partialResults.get(j));
            } // if partialResults...

            if (totResult) {
                HashMap<String, Object> record = new LinkedHashMap<>();
                fieldsToRetrieve.forEach(fieldName -> {
                    try {
                        record.put(fieldName, ReflectionHelper.getFieldValue(d, fieldName));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    } // catch
                });
//                result.add(record);
                result.addResultRecord(record);

                if (allSearchFieldsAreUnique)
                    return result;
            } // if totResult...
        } // while i...

        if (Debug.ON) System.out.printf("Fråga: %s, Antal poster i resultat: %d%n", this, result.getNumberOfHits());
        return result;
    } // execute

    private void addAllFields() {
        Database db = Database.getInstance();
        fieldsToRetrieve.remove(0);
        ReflectionHelper.getAllFields(db.getTable(fromTable).getDataClass()).forEach((k , v) -> fieldsToRetrieve.add(v.getName()));
    } // addAllFields

    private void checkFields(List<String> list) throws FieldNotFoundException {
        Table<?> t = Database.getInstance().getTable(fromTable);
        Map<String, Field> dataFields = ReflectionHelper.getAllFields(t.getDataClass());

        for (String fieldName : list) {
            if (dataFields.get(fieldName) == null) {
                throw new FieldNotFoundException(fieldName);
            } // if dataFields...
        } // for i...
    } // checkSelect

    @Override
    public String toString() {
        return String.format("Query: fromTable='%s', fields: %s, sort order: %s, clauses: %s", fromTable, fieldsToRetrieve, sortField, clauses.stream().map(WhereClause::toString).collect(Collectors.joining(", ")));
    } // toString
} // class Query