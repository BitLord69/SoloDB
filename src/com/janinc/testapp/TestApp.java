package com.janinc.testapp;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.Database;
import com.janinc.Table;
import com.janinc.exceptions.*;
import com.janinc.pubsub.Channel;
import com.janinc.pubsub.Message;
import com.janinc.pubsub.Subscriber;
import com.janinc.query.*;
import com.janinc.testapp.testdb.*;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;

public class TestApp implements Subscriber {
    private void printDBStatus() {
        Database db = Database.getInstance();
        System.out.printf("%n%s%n", db);

        db.getTables().forEach((k, v)-> {
            System.out.println("\nRecords in " + v.getName() + " :");
            ((Table)v).getRecords().forEach((kk, vv) -> System.out.println("Id: " + kk + " values: " + vv));
        });
    } // printDBStatus

    private void saveDisc(Disc d) {
        // TODO: 2020-02-21 Taktiktips - kolla med Dennis om det blir bättre att ha refresh-koden i själva db.save-metoden
        Database db = Database.getInstance();
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

    public String generateRandomString (int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    } // generateRandomString

    private void testValidation() {
        Database db = Database.getInstance();

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

        System.out.println("\nTrying to set a random name for disc '" + d + "'");
        d.setName(generateRandomString(10));
        saveDisc(d);
        System.out.println("Disc after name change: '" + d + "'");

        System.out.println("\nTrying to change the weight (randomly) for '" + d + "'...");
        d.setWeight(140 + (int)(Math.random() * 60));
        System.out.println("New proposed weight: " + d.getWeight());
        saveDisc(d);
        System.out.println("Weight after (hopefully) successful change: " + d + "\n");

        System.out.println("Testing to set a too long name!");
        d.setName(generateRandomString(305));
        saveDisc(d);

        System.out.println("\nTesting a mandatory field, creating a disc without a name!");
        d = new Disc("", "DC", 180, "", "", 2, 3, 4, 5, "PA");
        try {
            db.addRecord(d);
        } catch (ValidationException | InvocationTargetException | IllegalAccessException e) {
            System.out.println("Error after trying to add a disc with an empty name...\n" + e.getMessage());
        } // catch
    } // testValidation

    private void testQueries() {
        Query q = new Query();

        System.out.println(String.format("%s Testing the query engine! %s%n", "-".repeat(50), "-".repeat(50)));
        System.out.println("Trying to add a non-existing table...");

        try {
            q = q.from("Hej");
        } catch (TableNotFoundException e) {
            System.out.println(e.getMessage());
        } // catch

        QueryResult res = null;
        System.out.println("\nLooking for discs named 'tellus semper interdum' or heavier than 189 grams - not only unique field(s) used to search");
        try {
            q = new Query()
                    .from(Disc.class)
                    .sort("weight")
                    .select("name", "weight", "brandShadow")
                    .bindingOperator(BindingOperator.OR)
                    .where("name", "==", "tellus semper interdum")
                    .where("weight", ">", 189);
//                    .where("weight", "<", 188);
//                    .where("brandNew", "==", true);

            res = q.execute();
        } catch (TableNotFoundException | FieldNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } // catch

        System.out.println(q);
        System.out.println("Records in result: " + res.size());
        res.getResults().forEach(System.out::println);

        System.out.println("\nLooking for discs containing 'sed' or/and 'nulla' in the name... only unique field(s) used to search");
        try {
            q = new Query()
                    .from("disc")
//                    .select("*")
                    .select("name", "plastic", "plasticShadow", "weight", "brandNew", "brandShadow")
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
        res.getResults().forEach(System.out::println);
    } // testQueries

    private void subscribeToChangesInDb() {
        Database db = Database.getInstance();

        db.subscribe(Disc.class, Channel.ADD_RECORD, this);
        db.subscribe(Disc.class, Channel.EDIT_RECORD, this);
        db.subscribe(Manufacturer.class, Channel.ADD_RECORD, this);
        db.subscribe(Manufacturer.class, Channel.EDIT_RECORD, this);
        db.subscribe(Manufacturer.class, Channel.DELETE_RECORD, this);
    } // subscribeToChangesInDb

    private void testSubscriptions() {
        Database db = Database.getInstance();

        System.out.println(String.format("%s Testing the subscription service! %s%n", "-".repeat(50), "-".repeat(50)));

        Manufacturer m = new Manufacturer("A new manufacturer!", "ANM");
        try {
            db.addRecord(m);
        } catch (ValidationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } // catch

        m.setName("Changing the name...");
        try {
            db.save(m);
        } catch (ValidationException | InvocationTargetException | IllegalAccessException e) {
            db.refresh(m);
            e.printStackTrace();
        } // catch

        db.deleteRecord(m);
    } // testSubscriptions

    @Override
    public void update(Message message) {
        System.out.printf("%s%n", message);
    } // update

    public void initDb() {
        Database db =  Database.getInstance();
        db.addClass(Category.class);
        db.addClass(Manufacturer.class);
        db.addClass(Plastic.class);
        db.addClass(Disc.class);
    } // initDB

    public void run() throws ValidationException {
        Database.getInstance().initializeDB(this::initDb);
        TestDataFactory.createTestRecordsIfNone();

        printDBStatus();
        testValidation();
        testQueries();

        subscribeToChangesInDb();
        testSubscriptions();
    } // run
} // class TestApp