package com.janinc.testapp.testdb;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.annotations.StringField;
import com.janinc.annotations.Table;

@Table(name="cat")
public class Category extends DataObject {
    public static final long serialVersionUID = 42L;

    @StringField(name = "NAME", maxlength = 50, uniquevalue = true)
    private String name;

    @StringField(maxlength = 3, uniquevalue = true)
    private String abbreviation;

    public Category() { }
    public Category(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAbbreviation() { return abbreviation; }
    public void setAbbreviation(String abbreviation) { this.abbreviation = abbreviation; }

    @Override
    public String toString() {
        return name + ", " + abbreviation;
    }
} // class User