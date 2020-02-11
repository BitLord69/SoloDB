package com.janinc;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-13
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.annotations.StringField;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

import static com.janinc.Database.getInstance;

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

    public void resolve(DataObject d) {
        if (refTable == null)
            throw new IllegalStateException("Reference table cannot be null!" + this);

        Database db = Database.getInstance();

        Map<String, Field> dataFields = ReflectionHelper.getAllFields(d.getClass());
        Map<String, Field> refFields = ReflectionHelper.getAllFields(refTable.getDataClass());

        java.lang.reflect.Field sourceField = dataFields.get(key);
        sourceField.setAccessible(true);
        try {
            String value = (String) sourceField.get(d);

            // TODO: 2020-02-11 Do a proper search on the refKey later on; for now just looks for the id
            DataObject refRecord = db.getRecord((Class<? extends DataObject>) refTable.getDataClass(), value);
            if (refRecord != null) {
                java.lang.reflect.Field refField = refFields.get(refTextKey);
                refField.setAccessible(true);

                // TODO: 2020-02-11 Cast to the correct type; can be more than String!
                String replacementValue = (String) refField.get(refRecord);
                java.lang.reflect.Field replacementField = dataFields.get(shadowField);
                replacementField.setAccessible(true);
                replacementField.set(d, replacementValue);
            } // if refRecord...
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } // catch
    } // resolve

    @Override
    public String toString() {
        return String.format("Reference: refTable=%s, key=%s, refKey=%s, refTextKey=%s, shadowField=%s", refTable, key, refKey, refTextKey, shadowField);
    } // toString
} // class Reference
