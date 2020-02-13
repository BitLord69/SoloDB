package com.janinc.testapp.testdb;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-13
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.exceptions.ValidationException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestDataFactory {
    public static void createTestRecordsIfNone() {
        DiscDB db = DiscDB.getInstance();

        if (db.getNumberOfRecords(Manufacturer.class) == 0) createManufacturers();
        if (db.getNumberOfRecords("disc") == 0) createDiscs();
    } // createDiscsIfNone

    private static void createDiscs() {
        DiscDB db = DiscDB.getInstance();
        HashMap<String, DataObject> hm = db.getRecords("manu");
        Iterator<Map.Entry<String, DataObject>> i =  hm.entrySet().iterator();

        System.out.println("No discs in database -> creating some discs...");

        try {
            DataObject value = (DataObject) (i.next()).getValue();
            db.addRecord((DataObject)new Disc("Reko", value.getId(), 172, "White translucent", "Glow", 1.5f));

            value = (DataObject) (i.next()).getValue();
            db.addRecord((DataObject)new Disc("Reko", value.getId(), 174, "Lila", "K1", 1.2f));

            value = (DataObject) (i.next()).getValue();
            db.addRecord((DataObject)new Disc("Firebird", value.getId(), 176, "Orange", "Champion", 1.2f));
        } catch (ValidationException e) {
            e.printStackTrace();
        } // catch
    } // createDiscs

    private static void createManufacturers() {
        DiscDB db = DiscDB.getInstance();

        System.out.println("No manufacturers in the database, creating manufacturers first...");

        try {
            db.addRecord((DataObject) new Manufacturer("KastaPlast"));
            db.addRecord((DataObject) new Manufacturer("Innova"));
            db.addRecord((DataObject) new Manufacturer("Discraft"));
        } catch (ValidationException e) {
            e.printStackTrace();
        } // catch
    } // createManufacturers
} // class DataFactory
