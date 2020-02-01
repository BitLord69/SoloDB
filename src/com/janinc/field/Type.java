package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public enum Type {
    STRING ("String"),
    INT ("Int"),
    FLOAT ("float"),
    BOOLEAN ("boolean");

    private String name;

    Type(String name) {
        this.name = name;
    } // Type
} // enum Type
