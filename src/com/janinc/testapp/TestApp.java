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
import java.util.Map;

public class TestApp {
    public static void main(String[] args) {
        DiscDB.getInstance().initializeDB();

        DiscDB db = DiscDB.getInstance();

        TestDataFactory.createTestRecordsIfNone();

        System.out.printf("%n%s%n", db);

        System.out.println("\nRecords in manufacturer:");
        db.getRecords(Manufacturer.class).forEach((k, v) -> System.out.println("Id: " + k + " values: " + v));

        System.out.println("\nRecords in disc:");
        db.getRecords("disc").forEach((k, v) -> System.out.println("Id: " + k + " values: " + v));


        HashMap hm =  db.getRecords("disc");
        Map.Entry<String, DataObject> entry = (Map.Entry<String, DataObject>) hm.entrySet().iterator().next();
        DataObject value = entry.getValue();

        // Get the "first" disc and set the weight to an invalid value
        Disc d = (Disc) hm.get(value.getId());
        System.out.println("\nTrying to set an illegal value for disc '" + d + "'");
        d.setWeight(130);
        try {
            db.save(d);
        } // try
        catch (ValidationException e){
            System.out.println(e.getMessage());
            d.refresh();
        } // catch

        System.out.println("\nRecords in disc (all values still valid):");
        hm =  db.getRecords("disc");
        hm.forEach((k, v) -> System.out.println("Key: " + k + " v:" + v));

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
} // class TestApp