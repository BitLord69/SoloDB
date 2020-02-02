package com.janinc.testapp;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.Data;
import com.janinc.Database;
import com.janinc.query.*;
import com.janinc.exceptions.*;
import com.janinc.testapp.testdb.*;

import java.util.ArrayList;

public class TestApp {
    public static void main(String[] args) {
        System.out.println("Innan getInstance...");
        DiscDB db = DiscDB.getInstance();
        System.out.println("Efter getInstance...");

        System.out.println("TestDB : " + DiscDB.getInstance() + "\nDatabase: " + Database.getInstance());

        Query q = new Query();
        System.out.println("QueryDB: " + q.getQDB());

        try {
            q = q.from("Hej");
        } catch (TableNotFoundException e) {
            e.printStackTrace();
        }

//        DiscDB.getInstance().addRecord(new Disc("Reko", "KastaPlast", "172", "Purple", "K1"));
//        DiscDB.getInstance().addRecord(new Disc("Reko", "KastaPlast", "174", "White", "K1 Glow"));

        ArrayList<Data> res;

        try {
//            res = q.from(DiscTable.TABLE_NAME).select(Disc.NAME, Disc.BRAND).where(new WhereClause("Fel", Operator.EQUALS, "Champion Firebird")).execute();
            res = q.from(DiscTable.TABLE_NAME).select(Disc.NAME, Disc.BRAND).where(new WhereClause(Disc.NAME, Operator.EQUALS, "Champion Firebird")).execute();
        } catch (QueryException | TableNotFoundException | FieldNotFoundException e) {
            e.printStackTrace();
        } // catch

    } // main
} // class TestApp
