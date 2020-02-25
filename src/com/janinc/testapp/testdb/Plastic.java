package com.janinc.testapp.testdb;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.annotations.StringField;
import com.janinc.annotations.Table;

@Table(name="plastic")
public class Plastic extends DataObject {
    public static final long serialVersionUID = 42L;

    @StringField(name = "NAME", maxlength = 50, unique = true)
    private String name;

    @StringField(lookup=true, lookupTable=Manufacturer.class, lookupForeignKey="abbreviation", lookupForeignField="name", targetField="manuShadow")
    private String manufacturer;
    private transient String manuShadow;

    public Plastic() { }
    public Plastic(String name, String manufacturer)
    {
        this.name = name;
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public void setManuShadow(String manuShadow) { this.manuShadow = manuShadow; }

    @Override
    public String toString() {
        String s = manufacturer.isBlank() ?  " -- Manufacturer empty! -- " : manufacturer;
        return String.format("Name: '%s', Manufactured by: '%s' (%s)", name, manuShadow, s);
    } // toString
} // class User