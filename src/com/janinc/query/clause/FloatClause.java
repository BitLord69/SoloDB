package com.janinc.query.clause;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-14
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.query.Operator;

public class FloatClause extends WhereClause {
    private float comparator;

    public FloatClause(String fieldName, Operator operator, float comparator) {
        super(fieldName, operator);
        this.comparator = comparator;
    } // FloatClause

    public FloatClause(String fieldName, String operator, float comparator) {
        super(fieldName);
        this.comparator = comparator;
        if (!Operator.contains(operator))
            throw new IllegalArgumentException("Operator '" + operator + "' not recognized!");
        this.operator = Operator.get(operator);
        if (this.operator.compareTo(Operator.STARTS_WITH) > -1)
            throw new IllegalArgumentException("Operator '" + operator + "' not not supported for floats!");
    } // FloatClause

    @Override
    public boolean compare(String table, DataObject d) {
        return false;
    }
} // class FloatClause
