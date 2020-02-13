package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.exceptions.ValidationException;

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

    public void validateData(DataObject data) throws ValidationException {
        for (Field f : fields.values()) {
            f.validate(data);
        } // for f...
    } // validateData

    @Override
    public String toString() {
        String s = String.format("\t\t---------- FieldManager for table '%s' : number of  validation fields: %d ----------", dataClass.getSimpleName(), fields.size());
        return String.format("%s%n%s%n\t\t%s",
                s,
                fields
                        .entrySet()
                        .stream()
                        .map(o -> "\t\t\t" + ((Field)o.getValue()).toString())
                        .collect(Collectors.joining("\n")),
                "-".repeat(s.length()));
    } // toString
} // class FieldManager
