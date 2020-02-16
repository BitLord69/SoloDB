package com.janinc.query.clause;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-14
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/
import com.janinc.query.Operator;

public class StringClause extends WhereClause {
    public StringClause(String fieldName, Operator operator, String stringComparator) {
        super(fieldName, operator, stringComparator);
    } // StringClause

    public StringClause(String fieldName, String operator, String comparator) {
        super(fieldName, comparator);
        if (!Operator.contains(operator))
            throw new IllegalArgumentException("Operator '" + operator + "' not recognized!");
        this.operator = Operator.get(operator);
    } // StringClause

    @Override
    protected boolean equals(Object op1, Object op2) { return op1.equals(op2); } // equals

    @Override
    protected boolean notEquals(Object op1, Object op2) {
        return !op1.equals(op2);
    } // notEquals

    protected boolean greaterThan(Object op1, Object op2) {
        return ((String)op1).compareTo((String)op2) > 0;
    } // greaterThan

    protected boolean smallerThan(Object op1, Object op2) {
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
} // class StringClause
