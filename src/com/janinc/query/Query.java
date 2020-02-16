package com.janinc.query;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.*;
import com.janinc.exceptions.*;
import com.janinc.query.clause.*;
import com.janinc.testapp.testdb.DiscDB;
import com.janinc.util.Debug;
import com.janinc.util.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Query {
    private String fromTable = "";
    private List<String> fieldsToRetrieve = new ArrayList<>();
    private List<WhereClause> clauses = new ArrayList<>();
    private BindingOperator bindingOperation = BindingOperator.AND;

    public Query select(String ... fields) {
        this.fieldsToRetrieve.addAll(Arrays.asList(fields));
        return this;
    } // select

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

    public ArrayList<HashMap<String, Object>> execute() throws QueryException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (fromTable.equals("")) throw new QueryException("From table has not been set!");
        if (fieldsToRetrieve.equals("")) throw new QueryException("No call to select() has been made!");
        if (clauses.size() == 0) throw new QueryException("No Where-clauses present!");

        checkFields(fieldsToRetrieve);
        checkFields(clauses.stream().map(WhereClause::getFieldName).collect(Collectors.toList()));

        if (Debug.ON) System.out.println("In Query.execute - all fields checked OK!");

        DiscDB db = DiscDB.getInstance();
        ArrayList<HashMap<String, Object>> result = new ArrayList<>();
        Iterator<? extends Map.Entry<String, ? extends DataObject>> i = db.getIterator(fromTable);

        // TODO: 2020-02-16 Stop iterating if the field is unique and a record has been found
        while (i.hasNext()) {
            boolean totResult = false;
            DataObject d = (DataObject)(i.next()).getValue();
            ArrayList<Boolean> partialResults = new ArrayList<>();

            for (WhereClause c : clauses) {
                partialResults.add(c.compare(fromTable, d));
            } // for c...

            totResult = partialResults.get(0);
            if (partialResults.size() > 1) {
                for (int j = 1; j < partialResults.size(); j++) {
                    totResult = bindingOperation.function.test(totResult, partialResults.get(j));
                } // for j...
            } // if partialResults...

            if (totResult) {
                HashMap<String, Object> record = new HashMap<>();
                fieldsToRetrieve.forEach(fieldName -> {
                    try {
                        addResult(record, d, fieldName);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    } // catch
                });
                result.add(record);

                // TODO: 2020-02-16 Check if unique field - no need to look for more hits 
            } // if totResult...
        } // while i...

        if (Debug.ON) System.out.printf("Fråga: %s, Antal poster i resultat: %d%n", this, result.size());
        return result;
    } // execute

    private void addResult(HashMap<String, Object> result, DataObject d, String fieldName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        result.put(fieldName, ReflectionHelper.getFieldValue(fromTable, d, fieldName));
    } // addResult

    private void checkFields(List<String> list) throws FieldNotFoundException {
        Table t = DiscDB.getInstance().getTable(fromTable);
        Map<String, Field> dataFields = ReflectionHelper.getAllFields(t.getDataClass());

        for (String fieldName : list) {
            if (dataFields.get(fieldName) == null) {
                throw new FieldNotFoundException(fieldName);
            } // if dataFields...
        } // for i...
    } // checkSelect

    @Override
    public String toString() {
        return String.format("Query: fromTable='%s', fields: %s, clauses: %s", fromTable, fieldsToRetrieve, clauses.stream().map(WhereClause::toString).collect(Collectors.joining(", ")));
    } // toString
} // class Query