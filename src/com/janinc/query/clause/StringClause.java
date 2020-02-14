package com.janinc.query.clause;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-14
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.query.Operator;
import com.janinc.util.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StringClause extends WhereClause {
    private String stringComparator;

    public StringClause(String fieldName, Operator operator, String stringComparator) {
        super(fieldName, operator);
        this.stringComparator = stringComparator;
    } // StringClause

    public StringClause(String fieldName, String operator, String stringComparator) {
        super(fieldName);
        this.stringComparator = stringComparator;
        if (!Operator.contains(operator))
            throw new IllegalArgumentException("Operator '" + operator + "' not recognized!");
        this.operator = Operator.get(operator);
    } // StringClause

    private boolean equals(Object op1, Object op2) {
        return op1.equals(op2);
    } // equals

    private boolean notEquals(Object op1, Object op2) {
        return !op1.equals(op2);
    } // notEquals

    private boolean greaterThan(Object op1, Object op2) {
        return ((String)op1).compareTo((String)op2) > 0;
    } // greaterThan

    private boolean smallerThan(Object op1, Object op2) {
        return ((String)op1).compareTo((String)op2) < 0;
    } // smallerThan

    private boolean startsWith(Object op1, Object op2) {
        return ((String)op1).startsWith((String) op2);
    } // startsWith

    private boolean endsWith(Object op1, Object op2) {
        return ((String)op1).endsWith((String) op2);
    } // endsWith

    private boolean contains(Object op1, Object op2) {
        return ((String)op1).contains((String) op2);
    } // contains

    @Override
    public boolean compare(String table, DataObject d) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Field f = ReflectionHelper.getField(table, d, getFieldName());
        String value = (String) ReflectionHelper.runGetter(f, d);
        Method m = getCompareMethod();
        m.setAccessible(true);
        return (boolean) m.invoke(this, value, stringComparator);
    } // compare
} // class StringClause
