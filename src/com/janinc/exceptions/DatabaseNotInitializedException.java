package com.janinc.exceptions;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public class DatabaseNotInitializedException extends RuntimeException {

    public DatabaseNotInitializedException() {
        super(String.format("Database has not been initialized - please call initializeDB first!"));
    } // TableNotFoundException
} // class TableNotFoundException

