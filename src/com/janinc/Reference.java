package com.janinc;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-13
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public class Reference {
    private Table refTable;
    private String key;
    private String refKey;
    private String refTextKey;

    public Reference(Table refTable, String key, String refKey, String refTextKey){
        this.refTable = refTable;
        this.key = key;
        this.refKey = refKey;
        this.refTextKey = refTextKey;
    } // Reference

    public Table getRefTable() {
        return refTable;
    }

    public String getKey() {
        return key;
    }

    public String getRefKey() {
        return refKey;
    }

    public String getRefTextKey() {
        return refTextKey;
    }
} // class Reference
