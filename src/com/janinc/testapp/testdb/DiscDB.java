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

        addClass(Manufacturer.class);
        addClass(Disc.class);

        // Annotations for this as well???
//        addTable(HobbyTable.TABLE_NAME, new HobbyTable());
//        addTable(LocationTable.TABLE_NAME, new LocationTable());
//        addTable(LikeTable.TABLE_NAME, new LikeTable());
//
//        getTable(UserTable.TABLE_NAME).addReference(new Reference(getTable(HobbyTable.TABLE_NAME), User.HOBBIES, Data.ID, Hobby.NAME));
//        getTable(HobbyTable.TABLE_NAME).addReference(new Reference(getTable(LocationTable.TABLE_NAME), Hobby.LOCATIONS, Data.ID, Location.NAME));
    } // TestDB:TestDB

//    public static DiscDB getInstance() {
//        if (mInstance == null) {
//            mInstance = new DiscDB(DBNAME);
//        }
//        return (DiscDB) mInstance;
//    } // getInstance

    public static DiscDB getInstance() {
        if (mInstance == null) {
            mInstance = new DiscDB(DBNAME);
        } // if mInstance...

        return (DiscDB) mInstance;
    } // getInstance
} // class TestDB
