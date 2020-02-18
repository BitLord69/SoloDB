package com.janinc.query.clause;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.query.Operator;
import com.janinc.util.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class WhereClause {
    private String fieldName;
    protected Operator operator;
    protected Object searchTerm;

    protected WhereClause(String fieldName) {
        this.fieldName = fieldName;
    }

    protected WhereClause(String fieldName, Object searchTerm) {
        this(fieldName);
        this.searchTerm = searchTerm;
    }

    protected WhereClause(String fieldName, Operator operator, Object searchTerm) {
        this(fieldName, searchTerm);
        this.operator = operator;
    }

    public String getFieldName() { return fieldName; }
    public Operator getOperator() { return operator; }

    public boolean compare(String table, DataObject d) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Field f = ReflectionHelper.getField(d, getFieldName());
        Object value = ReflectionHelper.runGetter(f, d);
        Method m = ReflectionHelper.getMethod(this.getClass(), operator.getName());
        if (m == null) throw new NoSuchMethodException();

        m.setAccessible(true);
        return (boolean) m.invoke(this, value, searchTerm);
    } // compare

    protected boolean equals(Object op1, Object op2) { return op1 == op2; } // equals
    protected boolean notEquals(Object op1, Object op2) { return op1 != op2; } // notEquals

    @Override
    public String toString() {
        return String.format("[Type: %s, fieldName: '%s', operator: '%s', searchTerm: '%s']", getClass().getSimpleName(), fieldName, operator, searchTerm);
    } // toString
} // class WhereClause