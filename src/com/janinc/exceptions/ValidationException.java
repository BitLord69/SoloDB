package com.janinc.exceptions;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public class ValidationException extends Exception {

    public ValidationException(String field, String message) {
        super(String.format("Validation error for field '%s': %s", field, message));
    } // DBNotInitializedException
} // class DBNotInitializedException

