package com.janinc.query;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public enum Operator {
    EQUALS ("equals"),
    NOT_EQUALS ("notequals"),
    STARTS_WITH ("startswith"),
    ENDS_WITH ("endswith"),
    CONTAINS ("contains");

    private String name;

    Operator(String name) {
        this.name = name;
    } // Operator::Operator

    @Override
    public String toString() {
        return name;
    } // toString
} // enum Operator
