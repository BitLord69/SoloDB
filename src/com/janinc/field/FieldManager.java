package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.exceptions.ValidationException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

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

    public boolean isFieldUnique(String field) {
        return getField(field).isUnique();
    } // isFieldUnique

    public void updateDirtyFields(DataObject data) {
        fields.values().forEach(f -> f.updateDirtyField(data));
    } // setDirtyValues

    public void validateData(DataObject data) throws ValidationException, InvocationTargetException, IllegalAccessException {
        for (Field f : fields.values()) {
            f.validate(data);
        } // for f...
    } // validateData

    @Override
    public String toString() {
        String s = String.format("\t\t---------- FieldManager for table '%s' : number of  validation fields: %d ----------", dataClass.getSimpleName(), fields.size());
        return String.format("%s%n%s%n\t\t%s",
                s,
                fields.values()
                      .stream()
                      .map(field -> "\t\t\t" + field.toString())
                      .collect(Collectors.joining("\n")),
                "-".repeat(s.length()));
    } // toString
} // class FieldManager
