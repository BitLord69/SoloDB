package com.janinc;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-13
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.annotations.StringField;

public class Reference {
    private Table<? extends DataObject> refTable;
    private String key;
    private String refKey;
    private String refTextKey;
    private String shadowField;

    public Reference(Table<? extends DataObject> refTable, String key, String refKey, String refTextKey, String shadowField){
        this.refTable = refTable;
        this.key = key;
        this.refKey = refKey;
        this.refTextKey = refTextKey;
        this.shadowField = shadowField;
    } // Reference

    public Reference(String name, StringField annotation) {
        this(Database.getInstance().getTable(annotation.lookupTable()),
                name,
                annotation.lookupForeignKey(),
                annotation.lookupForeignField(),
                annotation.shadowField());
    } // Reference

    public Table<? extends DataObject> getRefTable() {
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

    public String getShadowField() { return shadowField; }
} // class Reference
