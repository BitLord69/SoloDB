package com.janinc.testapp;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.Database;
import com.janinc.exceptions.TableNotFoundException;
import com.janinc.query.Query;
import com.janinc.testapp.testdb.DiscTable;
import com.janinc.testapp.testdb.TestDB;

public class TestApp {
    public static void main(String[] args) {
        System.out.println("Innan getInstance...");
        TestDB db = TestDB.getInstance();
        System.out.println("Efter getInstance...");

        System.out.println("TestDB : " + TestDB.getInstance() + " Database: " + Database.getInstance());

        Query q = new Query();
        System.out.println("QueryDB: " + q.getQDB());

        try {
            q = q.from("Hej");
        } catch (TableNotFoundException e) {
            e.printStackTrace();
        }

        try {
            q = q.from(DiscTable.TABLE_NAME);
        } catch (TableNotFoundException e) {
            e.printStackTrace();
        }
    }
}
