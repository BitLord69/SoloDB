package com.janinc.testapp;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.Table;
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

        db.getTables().forEach((k, v)-> {
            System.out.println("\nRecords in " + v.getName() + " :");
            ((Table)v).getRecords().forEach((kk, vv) -> System.out.println("Id: " + kk + " values: " + vv));
        });
    } // printDBStatus

    private static void testValidation() {
        DiscDB db = DiscDB.getInstance();

        System.out.println(String.format("%n%s Validation testing! %s%n", "-".repeat(50), "-".repeat(50)));

        HashMap<String, ? extends DataObject> hm = db.getRecords("disc");
        if (hm.size() == 0){
            System.out.println("No records in the disc-table :(\n");
            return;
        } // if hm...

        Map.Entry<String, ? extends DataObject> entry = hm.entrySet().iterator().next();
        DataObject value = entry.getValue();

        // Get the "first" disc and set the weight to an invalid value
        Disc d = (Disc) hm.get(value.getId());
        System.out.println("Trying to set an illegal weight for disc '" + d + "'");
        d.setWeight(130);
        saveDisc(d);
        System.out.println("Disc after illegal value: '" + d + "'");

        System.out.println("\nTrying to set an already used name for disc '" + d + "'");
        d.setName("non velit donec");
        saveDisc(d);
        System.out.println("Disc after illegal name: '" + d + "'");

        System.out.println("\nTrying to change the weight (randomly) for '" + d + "'...");
        d.setWeight(140 + (int)(Math.random() * 60));
        System.out.println("New proposed weight: " + d.getWeight());
        saveDisc(d);
        System.out.println("Weight after (hopefully) successful change: " + d + "\n");

        System.out.println("Testing a mandatory field, creating a disc without a name!");
        d = new Disc("", "DC", 180, "", "", 2, 3, 4, 5, "PA");
        try {
            db.addRecord(d);
        } catch (ValidationException | InvocationTargetException | IllegalAccessException e) {
            System.out.println("Error after trying to add a disc with an empty name....\n" + e.getMessage());
        } // catch
    } // testValidation

    private static void saveDisc(Disc d) {
        DiscDB db = DiscDB.getInstance();
        try {
            db.save(d);
        } // try
        catch (ValidationException e){
            System.out.println(e.getMessage());
            db.refresh(d);
        } // catch
        catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } // catch
    } // saveDisc

    private static void testQueries() {
        Query q = new Query();

        System.out.println(String.format("%s Testing the query engine! %s%n", "-".repeat(50), "-".repeat(50)));
        System.out.println("Trying the add a non-existing table...");

        try {
            q = q.from("Hej");
        } catch (TableNotFoundException e) {
            System.out.println(e.getMessage());
        } // catch

        ArrayList<HashMap<String, Object>> res = new ArrayList<>();
        System.out.println("\nLooking for discs named 'tellus semper interdum' or heavier than 189 grams - not only unique field(s) used to search");

        try {
            q = new Query()
                    .from(Disc.class)
                    .select("name", "weight", "brandShadow")
                    .bindingOperator(BindingOperator.OR)
                    .where("name", "==", "tellus semper interdum")
                    .where("weight", ">", 189);
//                    .where("weight", "<", 188);
//                    .where("brandNew", "==", true);

            res = q.execute();
        } catch (TableNotFoundException | QueryException | FieldNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } // catch

        System.out.println(q);
        System.out.println("Records in result: " + res.size());
        res.forEach(System.out::println);

        System.out.println("\nLooking for discs containing 'sed' or/and 'nulla' in the name... only unique field(s) used to search");
        try {
            q = new Query()
                    .from(Disc.class)
//                    .select("*")
                    .select("name", "plastic", "plasticShadow", "weight", "brandShadow")
                    .bindingOperator(BindingOperator.OR)
//                    .bindingOperator(BindingOperator.AND)
                    .where("name", Operator.CONTAINS, "sed")
                    .where("name", "><", "nulla");
            res = q.execute();
        } catch (TableNotFoundException | QueryException | FieldNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } // catch

        System.out.println(q);
        System.out.println("Records in result: " + res.size());
        res.forEach(System.out::println);
    } // testQueries

    public static void main(String[] args) throws ValidationException {
        DiscDB.getInstance().initializeDB();
        TestDataFactory.createTestRecordsIfNone();

        printDBStatus();
        testValidation();
        testQueries();
    } // main
} // class TestApp