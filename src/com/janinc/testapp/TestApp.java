package com.janinc.testapp;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.Table;
import com.janinc.testapp.testdb.*;

public class TestApp {
    public static void main(String[] args) {
        DiscDB.getInstance().initializeDB();

        DiscDB db = DiscDB.getInstance();

        Table t1 = db.getTable("disc");
        Table t2 = db.getTable("manu");

        System.out.printf("DB%n%s%nTable 1: %s, table2: %s%n%n", db, t1.getName(), t2.getName());

        // Uncomment the two rows below to create a few records
//        createDiscs();
//        createManufacturers();

        db.getRecords(Disc.class).forEach((k, v) -> System.out.println("Id: " + k + " values: " + v));
        db.getRecords(Manufacturer.class).forEach((k, v) -> System.out.println("Id: " + k + " values: " + v));

//        Disc d = (Disc) hm.get("disc/1580826688107.row");
//        d.setColor("yellow");
//        d.setWeight(176);
//        db.save(d);

//        hm =  db.getRecords("disc");
//        hm.forEach((k, v) -> System.out.println("Key: " + k + " v:" + v));

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
        db.addRecord((DataObject)new Disc("Reko", "KastaPlast", 172, "White translucent", "Glow"));
        db.addRecord((DataObject)new Disc("Reko", "KastaPlast", 174, "Lila", "K1"));
    }

    private static void createManufacturers() {
        DiscDB db = DiscDB.getInstance();
        db.addRecord((DataObject)new Manufacturer("KastaPlast"));
        db.addRecord((DataObject)new Manufacturer("Innova"));
        db.addRecord((DataObject)new Manufacturer("Discraft"));
    }
} // class TestApp
