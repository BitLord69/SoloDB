package com.janinc.exceptions;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public class DBNotInitializedException extends Exception {

    public DBNotInitializedException() {
        super(String.format("Teh database has not been initialized! Please call 'Database.initialize()' first!"));
    } // DBNotInitializedException
} // class DBNotInitializedException

