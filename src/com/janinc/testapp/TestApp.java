package com.janinc.testapp;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.Table;
import com.janinc.exceptions.ValidationException;
import com.janinc.testapp.testdb.*;

import java.util.HashMap;

public class TestApp {
    public static void main(String[] args) {
        DiscDB.getInstance().initializeDB();

// Uncomment the two rows below to create a few records
//        createDiscs();
//        createManufacturers();

        DiscDB db = DiscDB.getInstance();

        Table t1 = db.getTable("manu");
        Table t2 = db.getTable("disc");

        System.out.printf("%nDB%n%s%nTable 1: %s, table2: %s%n%n", db, t1.getName(), t2.getName());

        System.out.println(t1.getFieldManager() + "\n\n" + t2.getFieldManager() + "\n");

        db.getRecords(Disc.class).forEach((k, v) -> System.out.println("Id: " + k + " values: " + v));
        db.getRecords(Manufacturer.class).forEach((k, v) -> System.out.println("Id: " + k + " values: " + v));

        HashMap hm =  db.getRecords("disc");
//        hm.forEach((k, v) -> System.out.println("Key: " + k + " v:" + v));

//        Disc d = (Disc) hm.get("disc/1581433750751.row");
//        d.setWeight(130);
//        try {
//            db.save(d);
//        }
//        catch (ValidationException e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println(d);

//        System.out.println("Innan getInstance...");
//        DiscDB db = DiscDB.getInstance();
//        System.out.println("Efter getInstance...");

//        System.out.println("TestDB : " + DiscDB.getInstance() + "\nDatabase: " + Database.getInstance());

//        Query q = new Query();
//        System.out.println("QueryDB: " + q.getQDB());

//        try {
//            q = q.from("Hej");
//        } catch (TableNotFoundException e) {
//            e.printStackTrace();
//        }

//        ArrayList<Data> res;
/*
        try {
//            res = q.from(DiscTable.TABLE_NAME).select(Disc.NAME, Disc.BRAND).where(new WhereClause("Fel", Operator.EQUALS, "Champion Firebird")).execute();
            res = q
                    .from(DiscTable.TABLE_NAME)
                    .select(Disc.NAME, Disc.BRAND)
                    .where(
                            Operator.equals(Disc.NAME, "Champion Firebird")
                            new WhereClause(Disc.NAME, Operator.EQUALS, "Champion Firebird")
                    ).execute();
        } catch (QueryException | TableNotFoundException | FieldNotFoundException e) {
            e.printStackTrace();
        } // catch
*/
    } // main

    private static void createDiscs() {
        DiscDB db = DiscDB.getInstance();
        try {
            db.addRecord((DataObject)new Disc("Reko", "manu/1581430055274.row", 172, "White translucent", "Glow", 1.5f));
            db.addRecord((DataObject)new Disc("Reko", "manu/1581430055274.row", 174, "Lila", "K1", 1.2f));
            db.addRecord((DataObject)new Disc("Firebird", "manu/1581430058047.row", 176, "Orange", "Champion", 1.2f));
        } catch (ValidationException e) {
            e.printStackTrace();
        } // catch
    } // createDisc

    private static void createManufacturers() {
        DiscDB db = DiscDB.getInstance();
        try {
            db.addRecord((DataObject)new Manufacturer("KastaPlast"));
            db.addRecord((DataObject)new Manufacturer("Innova"));
            db.addRecord((DataObject)new Manufacturer("Discraft"));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
} // class TestApp
