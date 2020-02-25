package com.janinc.query;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import java.util.Map;
import java.util.HashMap;

public enum Operator {
    EQUALS ("equals", "=="),
    NOT_EQUALS ("notEquals", "!="),
    GREATER_THAN ("greaterThan", ">"),
    SMALLER_THAN ("smallerThan", "<"),
    STARTS_WITH ("startsWith", "<<"),
    ENDS_WITH ("endsWith", ">>"),
    CONTAINS ("contains", "><");

    private String name;
    private String stringRepresentation;

    private static Map<String, Operator> stringValues = new HashMap<>();

    static{
        for (Operator op : Operator.values()) {
            stringValues.put(op.getStringRepresentation(), op);
        } // for op...
    } // static initializer

    public static Operator get(String operator) { return stringValues.get(operator); }

    public static boolean contains(String value){
        return stringValues.get(value) != null;
    } // contains

    Operator(String name, String stringRepresentation) {
        this.name = name;
        this.stringRepresentation = stringRepresentation;
    } // Operator::Operator

    public String getName() { return name; }

    public String getStringRepresentation() { return stringRepresentation; }

    @Override
    public String toString() {
        return name + " (" + stringRepresentation + ")";
    } // toString
} // enum Operator