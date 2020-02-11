package com.janinc;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-04
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.annotations.StringField;

public abstract class DataObject implements java.io.Serializable {
    @StringField(name = "id", uniquevalue = true)
    private String id = "";

    public String getId() { return id; }
    protected  void setId(String id) { this.id = id; }
} // class DataObject
