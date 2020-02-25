package com.janinc.query.clause;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-14
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.query.Operator;

public class BooleanClause extends WhereClause {
    private boolean comparator;

    public BooleanClause(String fieldName, Operator operator, boolean comparator) {
        super(fieldName, operator);
        this.comparator = comparator;
    } // BooleanClause

    public BooleanClause(String fieldName, String operator, boolean comparator) {
        super(fieldName);
        this.comparator = comparator;
        if (!Operator.contains(operator))
            throw new IllegalArgumentException("Operator '" + operator + "' not recognized!");
        this.operator = Operator.get(operator);
        if (this.operator.compareTo(Operator.GREATER_THAN) > -1)
            throw new IllegalArgumentException("Operator '" + operator + "' not not supported for booleans!");
    } // BooleanClause

    @Override
    public boolean compare(String table, DataObject d) {
        return false;
    } // compare
} // class BooleanClause
