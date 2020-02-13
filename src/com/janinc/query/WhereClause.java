package com.janinc.query;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public class WhereClause {
    private String fieldName;
    private Operator operator;
    private String  comparator;

    public WhereClause(String fieldName, Operator operator, String comparator) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.comparator = comparator;
    } // WhereClause

    public String getFieldName() {
        return fieldName;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getComparator() {
        return comparator;
    }

    @Override
    public String toString() {
        return String.format("WhereClause: fieldName: '%s', operator: '%s', comparator: '%s'", fieldName, operator, comparator);
    } // toString
} // class WhereClause
