package com.janinc.testapp.testdb;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.annotations.IntField;
import com.janinc.annotations.StringField;
import com.janinc.annotations.Table;

@Table(name="manu")
public class Manufacturer extends DataObject {
    public static final long serialVersionUID = 42L;

    @StringField(name = "NAME", maxlength = 300, uniquevalue = true)
    private String name;

    public Manufacturer() {
    }

    public Manufacturer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
} // class User