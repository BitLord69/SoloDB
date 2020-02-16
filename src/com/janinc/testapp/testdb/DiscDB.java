package com.janinc.testapp.testdb;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.Database;

public class DiscDB extends Database {
    private final static String DBNAME = "TESTDB";

    private DiscDB(String name) {
        super(name);

        addClass(Category.class);
        addClass(Manufacturer.class);
        addClass(Plastic.class);
        addClass(Disc.class);
    } // TestDB:TestDB

    public static DiscDB getInstance() {
        if (mInstance == null) {
            mInstance = new DiscDB(DBNAME);
        } // if mInstance...

        return (DiscDB) mInstance;
    } // getInstance
} // class TestDB
