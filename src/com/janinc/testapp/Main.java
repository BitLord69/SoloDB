package com.janinc.testapp;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-20
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.exceptions.ValidationException;
import com.janinc.testapp.testdb.DiscDB;
import com.janinc.testapp.testdb.TestDataFactory;

public class Main {
    public static void main(String[] args) throws ValidationException {
        new TestApp().run();
    } // main
} // class Main
