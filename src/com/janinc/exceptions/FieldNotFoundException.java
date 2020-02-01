package com.janinc.exceptions;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public class FieldNotFoundException extends Exception {

    public FieldNotFoundException(String table) {
        super(String.format("Field '%s' not found!", table));
    } // TableNotFoundException
} // class TableNotFoundException

