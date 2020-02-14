package com.janinc.query.clause;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.query.Operator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class WhereClause {
    private String fieldName;
    protected Operator operator;

    public abstract boolean compare(String table, DataObject d) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    protected WhereClause(String fieldName) {
        this.fieldName = fieldName;
    }

    protected WhereClause(String fieldName, Operator operator) {
        this.fieldName = fieldName;
        this.operator = operator;
    }

    public String getFieldName() { return fieldName; }

    public Operator getOperator() { return operator;}

    protected Method getCompareMethod() throws NoSuchMethodException {
        return this.getClass().getDeclaredMethod(operator.getName(), Object.class, Object.class);
    } // getMethod

    @Override
    public String toString() {
        return String.format("[Type: %s, fieldName: '%s', operator: '%s']", getClass().getSimpleName(), fieldName, operator);
    } // toString
} // class WhereClause
