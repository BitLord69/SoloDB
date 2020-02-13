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

import java.util.ArrayList;
import java.util.List;

public class Query {
    private String[] fields;
    private String fromTable;
    private List<WhereClause> clauses = new ArrayList<>();

    public Query select(String ... fields) {
        this.fields = fields;
        return this;
    } // select

    public Query from(String table) throws TableNotFoundException {
        fromTable = table;

        if (!Database.getInstance().tableExists(table))
            throw new TableNotFoundException(table);

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
        checkField(clauses.stream().map(c -> c.getFieldName()).toArray(String[]::new));

        return null;
    } // execute

    private void checkField(String[] fields) throws FieldNotFoundException {
        Table t = DiscDB.getInstance().getTable(fromTable);
        FieldManager fm = t.getFieldManager();

        for (int i = 0; i < fields.length; i++) {
            if (!fm.fieldExists(fields[i])) {
                throw new FieldNotFoundException(fields[i]);
            } // if !fm...
        } // for i...
    } // checkSelect

    public Database getQDB() {
        return Database.getInstance();
    } // getQDB
} // class Query

/*
Query = new Query().
    select(User.ID, User.Name, User.EMAIL).
    FROM(UserTable).
    WHERE(new WhereClause(User.Name, OPERATOR.EQUALS, searchTerm)).
    execute();

  possibly

Query = new Query().
    select(User.ID, User.Name, User.EMAIL).
    FROM(UserTable).
    WHERE(new WhereClause(User.Name, new Operator(OPERATOR.EQUALS), searchTerm)).
    execute(); using the Strategy pattern for the operator

*/