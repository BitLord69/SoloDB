package com.janinc.testapp.testdb;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-13
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.Database;
import com.janinc.exceptions.ValidationException;
import com.janinc.util.TextUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestDataFactory {
    public static void createTestRecordsIfNone() {
        Database db = Database.getInstance();

        if (db.getNumberOfRecords(Plastic.class) == 0) createPlastic();
        if (db.getNumberOfRecords("cat") == 0) createCategories();
        if (db.getNumberOfRecords(Manufacturer.class) == 0) createManufacturers();
        if (db.getNumberOfRecords("disc") == 0) createDiscs();
    } // createDiscsIfNone

    private static void createPlastic() {
        String regex = "[ ]{5}([\\w\\pS -]+)(\\[.{2,3}\\])";
        Database db = Database.getInstance();

        System.out.println("No plastic in the database, creating plastic...");

        List<String> wordsList;
        Path path = Paths.get("plastic.txt");
        try {
            wordsList = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } // catch

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(wordsList.get(0));
        while(m.find()) {
            Plastic plastic = new Plastic(m.group(1).trim(), m.group(2).replace("[", "").replaceAll("]", "").trim());
            try {
                db.addRecord(plastic);
            } catch (ValidationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            } // catch
            System.out.println(plastic);
        } // while m...
    } // createPlastic

    private static void createCategories() {
        Database db = Database.getInstance();

        System.out.println("No categories in the database, creating categories...");

        try {
            db.addRecord(new Category("Putt & Approach", "PA"));
            db.addRecord(new Category("Midrange driver", "MD"));
            db.addRecord(new Category("Fairway driver", "FD"));
            db.addRecord(new Category("Distance driver", "DD"));
        } catch (ValidationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } // catch
    } // createCategories

    private static void createManufacturers() {
        Database db = Database.getInstance();

        System.out.println("No manufacturers in the database, creating manufacturers first...");

        try {
            db.addRecord(new Manufacturer("KastaPlast", "KP"));
            db.addRecord(new Manufacturer("Innova", "IN"));
            db.addRecord(new Manufacturer("Discraft", "DC"));
            db.addRecord(new Manufacturer("Latitude 64", "L64"));
            db.addRecord(new Manufacturer("Dynamic Discs", "DD"));
            db.addRecord(new Manufacturer("Discmania", "DM"));
            db.addRecord(new Manufacturer("Westside", "WS"));
            db.addRecord(new Manufacturer("MVP", "MVP"));
            db.addRecord(new Manufacturer("Prodigy", "PDY"));
            db.addRecord(new Manufacturer("Legacy", "LD"));
            db.addRecord(new Manufacturer("Gateway", "GW"));
            db.addRecord(new Manufacturer("Millenium", "MN"));
        } catch (ValidationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } // catch
    } // createManufacturers

    private static void createDiscs() {
        Database db = Database.getInstance();

        System.out.println("No plastic in the database, creating plastic...");

        List<String> wordsList;
        Path path = Paths.get("mockup_discs.txt");
        try {
            wordsList = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } // catch

        Object[] plastics = db.getRecords("plastic").values().toArray();
        Object[] categories = db.getRecords("cat").values().toArray();

        for (String s : wordsList) {
            String[] parts =  s.split(",");
            Random rand = new Random();
            Plastic plastic = (Plastic)plastics[rand.nextInt(plastics.length)];
            Category cat = (Category) categories[rand.nextInt(categories.length)];
            Disc disc = new Disc(TextUtil.titleCase(parts[0]),
                                plastic.getManufacturer(),
                                Integer.parseInt(parts[1]),
                                parts[2],
                                plastic.getId(),
                                Integer.parseInt(parts[3]),
                                Integer.parseInt(parts[4]),
                                Integer.parseInt(parts[5]),
                                Integer.parseInt(parts[6]),
                                cat.getAbbreviation());
            try {
                db.addRecord(disc);
            } catch (ValidationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            } // catch
            System.out.println(disc);
        } // for s...
    } // createDiscs
} // class DataFactory