package com.janinc.query;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-14
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

public enum BindingOperator {
    AND ("and", BindingOperator::and),
    OR ("or", BindingOperator::or);

    private String operator;
    BiPredicate function;

    private static Map<String, Operator> stringValues = new HashMap<>();

    private static boolean and(Object op1, Object op2) {
        return (boolean)op1 && (boolean)op2;
    } // and

    private static boolean or(Object op1, Object op2) {
        return (boolean)op1 || (boolean)op2;
    } // or

    static{
        for (Operator op : Operator.values()) {
            stringValues.put(op.getStringRepresentation(), op);
        } // for op...
    } // static inilializer

    public static Operator get(String operator) { return stringValues.get(operator); }

    BindingOperator(String operator, BiPredicate function) {
        this.operator = operator;
        this.function = function;
    } //BindingOperator:BindingOperator
} // class BindingOperator
