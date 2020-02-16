package com.janinc.testapp;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.exceptions.*;
import com.janinc.query.*;
import com.janinc.testapp.testdb.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestApp {
    private static void printDBStatus() {
        DiscDB db = DiscDB.getInstance();
        System.out.printf("%n%s%n", db);

        System.out.println("\nRecords in manufacturer:");
        db.getRecords(Manufacturer.class).forEach((k, v) -> System.out.println("Id: " + k + " values: " + v));

        System.out.println("\nRecords in disc:");
        db.getRecords("disc").forEach((k, v) -> System.out.println("Id: " + k + " values: " + v));
    } // printDBStatus

    private static void testValidation() {
        DiscDB db = DiscDB.getInstance();

        HashMap<String, ? extends DataObject> hm = db.getRecords("disc");
        Map.Entry<String, ? extends DataObject> entry = hm.entrySet().iterator().next();
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
        hm = (HashMap<String, ? extends DataObject>) db.getRecords("disc");
        hm.forEach((k, v) -> System.out.println("Key: " + k + " v:" + v));

        d.setWeight(140 + (int)(Math.random() * 60));
        try {
            db.save(d);
        } // try
        catch (ValidationException e){
            System.out.println(e.getMessage());
            d.refresh();
        } // catch
        System.out.println("\nWeight after (hopefully) successful change: " + d + "\n");
    } // testValidation

    private static void testQueries() {
        Query q = new Query();

        try {
            q = q.from("Hej");
        } catch (TableNotFoundException e) {
            e.printStackTrace();
        } // catch

        ArrayList<HashMap<String, Object>> res = new ArrayList<>();

        try {
            q = new Query()
                    .from(Disc.class)
                    .select("name", "weight", "brandShadow")
                    .bindingOperator(BindingOperator.OR)
                    .where("name", "!=", "Firebird")
                    .where("weight", ">", 173);
//                    .where("name", Operator.EQUALS, "Firebird");
//                    .where("brandNew", "==", true);
//                    .where(new WhereClause("name", "===", "Champion Firebird"));
            res = q.execute();
        } catch (TableNotFoundException | QueryException | FieldNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } // catch

        System.out.println(q);
        System.out.println("Records in result: " + res.size());
        res.forEach(System.out::println);
    } // testQueries

    public static void main(String[] args) {
        DiscDB.getInstance().initializeDB();
        TestDataFactory.createTestRecordsIfNone();

        printDBStatus();
        testValidation();
        testQueries();
    } // main
} // class TestApp