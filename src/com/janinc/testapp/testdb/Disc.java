package com.janinc.testapp.testdb;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/
import com.janinc.Data;

import java.util.HashMap;

public class Disc extends Data {

    public static final String NAME = "name";
    public static final String BRAND = "brand";
    public static final String WEIGHT = "weight";
    public static final String COLOR = "color";

    public Disc(String fileName) {
        super(fileName);
    }

    public Disc(HashMap<String, String> hm){
        super("");
        setName(hm.get(NAME));
    } // User

    public Disc(String name, String brand, String weight, String color) {
        this("", name, brand, weight, color);
    }

    public Disc(String fileName, String name, String brand, String weight, String color) {
        super(fileName);
        setName(name);
        setBrand(brand);
        setWeight(weight);
        setColor(color);
    }

    @Override
    public boolean load() {
        super.load();
        return true;
    } // load

    @Override
    public String getFolderName() {
        return DiscTable.TABLE_NAME;
    } // getFolderName

    public String getName() {
        return (String)getData().get(Disc.NAME);
    }

    public void setName(String name) {
        getData().put(Disc.NAME, name);
    }

    public String getBrand() {
        return (String)getData().get(Disc.BRAND);
    }

    public void setBrand(String brand) {
        getData().put(Disc.BRAND, brand);
    }

    public String getWeight() {
        return (String)getData().get(Disc.WEIGHT);
    }

    public void setWeight(String weight) {
        getData().put(Disc.WEIGHT, weight);
    }

    public String getColor() {
        return (String)getData().get(Disc.COLOR);
    }

    public void setColor(String color) {
        getData().put(Disc.COLOR, color);
    }

    @Override
    public String toString() {
        return String.format("%s", getName());
    } // toString
} // class User
