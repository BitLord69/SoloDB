package com.janinc.query;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.Database;
import com.janinc.Table;
import com.janinc.exceptions.FieldNotFoundException;
import com.janinc.exceptions.TableNotFoundException;
import com.janinc.field.FieldManager;
import com.janinc.testapp.testdb.DiscDB;
import com.janinc.util.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Query {
    private String fromTable;
    private List<String> fields = new ArrayList<>();
    private List<WhereClause> clauses = new ArrayList<>();

    public Query select(String ... fields) {
        this.fields.addAll(Arrays.asList(fields));
        return this;
    } // select

    public Query from(String table) throws TableNotFoundException {
        if (!Database.getInstance().tableExists(table)) {
            throw new TableNotFoundException(table);
        } // if !Database...

        fromTable = table;
        return this;
    } // from

    public Query from(Class<? extends DataObject> dataClass) throws TableNotFoundException {
        Database db = Database.getInstance();
        if (!db.tableExists(dataClass)) {
            throw new TableNotFoundException(dataClass.toString());
        } // if !Database...

        fromTable = db.getTableName(dataClass);
        return this;
    } // from

    public Query where(WhereClause clause) {
        clauses.add(clause);
        return this;
    } // where

    public ArrayList<DataObject> execute() throws QueryException, FieldNotFoundException {
        if (fields.equals("")) throw new QueryException("From table not set!");
        if (clauses.size() == 0) throw new QueryException("No Where-clauses present!");

        checkField(fields);
        checkField(clauses.stream().map(WhereClause::getFieldName).collect(Collectors.toList()));

        System.out.println("In Query.execute - all fields checked OK!");
        return null;
    } // execute

    private void checkField(List<String> l) throws FieldNotFoundException {
        Table t = DiscDB.getInstance().getTable(fromTable);
        Map<String, Field> dataFields = ReflectionHelper.getAllFields(t.getDataClass());

        for (String f : l) {
            if (dataFields.get(f) == null) {
                throw new FieldNotFoundException(f);
            } // if dataFields...
        } // for i...
    } // checkSelect

    @Override
    public String toString() {
        return String.format("Query: fromTable='%s', fields: %s, clauses: %s", fromTable, fields, clauses.stream().map(WhereClause::toString).collect(Collectors.joining(", ")));
    } // toString
} // class Query