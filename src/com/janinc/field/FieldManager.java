package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;

import java.util.HashMap;
import java.util.Map;

public class FieldManager {
    private Map<String, Field> fields = new HashMap<>();
    private Class<?> dataClass;

    public FieldManager(Class<?> dataClass) {
        this.dataClass = dataClass;
    }

    public void addField(Field field) {
        fields.put(field.getName(), field);
    } // addField

    public void removeField(Field field) {
        fields.remove(field);
    } // removeField

    public Field getField(String field) {
        return fields.get(field);
    } // getField

    public boolean fieldExists(String field) {
        return fields.get(field) != null;
    } // fieldExists

    public Map<String, Field> getFields() {
        return fields;
    } // getFields

    public boolean validateData(DataObject data) {
        // TODO: 2020-01-30 Maybe replace the 'return false' with a custom exception to signal what went wrong
//        for (String key : data.getData().keySet()) {
//            if (fields.containsKey(key)) {
//                // TODO: 2020-01-30 Need to pass in the data to validate, but how? Should the Data class have the fields instead, which will contain the values as well?
//                if (!fields.get(key).validate(data.getData().get(key))) {
//                    return false;
//                }
//            } // if fields...
//            else
//                return false;
//        } // for key...

        return true;
    } // validateData
} // class FieldManager
