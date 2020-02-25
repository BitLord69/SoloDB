package com.janinc;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-13
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.query.*;
import com.janinc.exceptions.*;
import com.janinc.query.clause.*;
import com.janinc.util.ReflectionHelper;
import com.janinc.annotations.StringField;

import java.util.Map;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Reference {
    private Table<? extends DataObject> refTable;
    private String key;
    private String refKey;
    private String refTextKey;
    private String targetField;

    public Reference(Table<? extends DataObject> refTable, String key, String refKey, String refTextKey, String targetField){
        this.refTable = refTable;
        this.key = key;
        this.refKey = refKey;
        this.refTextKey = refTextKey;
        this.targetField = targetField;
    } // Reference

    public Reference(String name, StringField annotation) {
        this(Database.getInstance().getTable(annotation.lookupTable()),
                name,
                annotation.lookupForeignKey(),
                annotation.lookupForeignField(),
                annotation.targetField());
    } // Reference

    public String getKey() { return key; }
    public String getRefKey() { return refKey; }
    public String getRefTextKey() { return refTextKey; }
    public String getTargetField() { return targetField; }
    public Table<? extends DataObject> getRefTable() { return refTable; }

    private QueryResult resolveHelper(Table<? extends DataObject> table, DataObject d, String dataKey, String whereClauseKey, String selectKey) throws IllegalAccessException {
        if (table == null)
            throw new IllegalStateException("Reference table cannot be null!" + this);

        Database db = Database.getInstance();
        Map<String, Field> dataFields = ReflectionHelper.getAllFields(d.getClass());
        Field sourceField = dataFields.get(dataKey);
        sourceField.setAccessible(true);
        Object value = sourceField.get(d);
        Class<?> sourceClass = sourceField.getType();
        QueryResult res = null;

        try {
            WhereClause wc = createWhereClause(whereClauseKey, sourceClass, value);
            Query q = new Query().from(table.getName()).select(selectKey).where(wc);
            res = q.execute();
        } catch (TableNotFoundException | QueryException | FieldNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } // catch

        return res;
    } // resolveHelper

    public void resolve(DataObject d)  {
        QueryResult res = null;
        try {
            res = resolveHelper(refTable, d, key, refKey, refTextKey);
            if (res.size() > 0) {
                try {
                    Object replacementValue = res.getResults().get(0).get(refTextKey);
                    ReflectionHelper.setFieldValue(d, targetField, replacementValue);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } // catch
            } // if refRecord...
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } //  catch
     } // resolve

    public void refValueChanged(Table<? extends DataObject> tblChange, DataObject refData) {
        QueryResult res = null;
        try {
            res = resolveHelper(tblChange, refData, refKey, key, "id");
            res.getResults().forEach(hm -> {
                try {
                    Database db = Database.getInstance();
                    Object replacementValue = ReflectionHelper.getFieldValue(refData, refTextKey);
                    DataObject recToChange = db.getRecord((Class<? extends DataObject>) tblChange.getDataClass(), (String)hm.get("id"));
                    ReflectionHelper.setFieldValue(recToChange, targetField, replacementValue);
                    db.save(recToChange);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                } // catch
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } // catch
    } // refValueChanged

    public <D extends DataObject> boolean hasChildRecords(Table<D> table, DataObject d) throws IllegalAccessException {
        return resolveHelper(table, d, refKey, key, "id").size() > 0;
    } // hasChildRecords

    public <D extends DataObject> void cascadeDelete(Table<D> table, DataObject data) {
        QueryResult res = null;

        try {
            res = resolveHelper(table, data, refKey, key, "id");
            res.getResults().forEach(hm -> {
                Database db = Database.getInstance();
                DataObject recToDelete = db.getRecord((Class<? extends DataObject>) table.getDataClass(), (String)hm.get("id"));
                db.cascadeDelete(recToDelete);
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } // catch
    } // cascadeDelete

    private WhereClause createWhereClause(String key, Class<?> sourceClass, Object value) {
        WhereClause wc = null;

        if (sourceClass == int.class) {
            wc = new IntClause(key, "==", (int)value);
        }
        else if (sourceClass == float.class) {
            wc = new FloatClause(key, "==", (float)value);
        }
        else if (sourceClass == String.class) {
            wc = new StringClause(key, "==", (String)value);
        }

        return wc;
    } // createWhereClause

    @Override
    public String toString() {
        return String.format("\t\tReference: refTable=%s, key=%s, refKey=%s, refTextKey=%s, targetField=%s", refTable.getName(), key, refKey, refTextKey, targetField);
    } // toString
} // class Reference
