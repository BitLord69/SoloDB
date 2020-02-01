package com.janinc.testapp.testdb;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.Database;

public class TestDB extends Database {
    private final static String DBNAME = "TESTDB";

    protected TestDB(String name) {
        super(name);

        addTable(DiscTable.TABLE_NAME, new DiscTable<Disc>());

//        addTable(HobbyTable.TABLE_NAME, new HobbyTable());
//        addTable(LocationTable.TABLE_NAME, new LocationTable());
//        addTable(LikeTable.TABLE_NAME, new LikeTable());
//
//        getTable(UserTable.TABLE_NAME).addReference(new Reference(getTable(HobbyTable.TABLE_NAME), User.HOBBIES, Data.ID, Hobby.NAME));
//        getTable(HobbyTable.TABLE_NAME).addReference(new Reference(getTable(LocationTable.TABLE_NAME), Hobby.LOCATIONS, Data.ID, Location.NAME));
    } // TestDB:TestDB

    public static TestDB getInstance() {
        if (mInstance == null) {
            mInstance = new TestDB(DBNAME);
        }
        return (TestDB) mInstance;
    } // getInstance
} // class TestDB
