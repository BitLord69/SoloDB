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

public class FloatClause extends WhereClause {
    public FloatClause(String fieldName, Operator operator, float comparator) {
        super(fieldName, operator, comparator);
    } // FloatClause

    public FloatClause(String fieldName, String operator, float comparator) {
        super(fieldName, comparator);
        if (!Operator.contains(operator))
            throw new IllegalArgumentException("Operator '" + operator + "' not recognized!");
        this.operator = Operator.get(operator);
        if (this.operator.compareTo(Operator.STARTS_WITH) > -1)
            throw new IllegalArgumentException("Operator '" + operator + "' not not supported for floats!");
    } // FloatClause

    private boolean greaterThan(Object op1, Object op2) { return (float)op1 > (float)op2; } // greaterThan
    private boolean smallerThan(Object op1, Object op2) { return (float)op1 < (float)op2; } // smallerThan
} // class FloatClause
