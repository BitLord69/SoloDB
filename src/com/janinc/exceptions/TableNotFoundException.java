package com.janinc.exceptions;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public class TableNotFoundException extends RuntimeException {

    public TableNotFoundException(String table) {
        super(String.format("Table '%s' not found!", table));
    } // TableNotFoundException
} // class TableNotFoundException

