package com.janinc.exceptions;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.util.TextUtil;

public class DatabaseNotInitializedException extends RuntimeException {

    public DatabaseNotInitializedException() {
        super(String.format("Database has not been initialized - please call %s first!", TextUtil.pimpString("initializeDB", TextUtil.LEVEL_INFO)));
    } // TableNotFoundException
} // class TableNotFoundException

