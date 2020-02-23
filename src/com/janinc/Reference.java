package com.janinc;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-13
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.annotations.StringField;
import com.janinc.exceptions.FieldNotFoundException;
import com.janinc.exceptions.TableNotFoundException;
import com.janinc.query.Query;
import com.janinc.query.QueryException;
import com.janinc.query.QueryResult;
import com.janinc.query.clause.FloatClause;
import com.janinc.query.clause.IntClause;
import com.janinc.query.clause.StringClause;
import com.janinc.query.clause.WhereClause;
import com.janinc.util.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

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

    public String getTargetField() { return targetField; }

    public void resolve(DataObject d) {
        if (refTable == null)
            throw new IllegalStateException("Reference table cannot be null!" + this);

        Database db = Database.getInstance();

        Map<String, Field> dataFields = ReflectionHelper.getAllFields(d.getClass());
        Map<String, Field> refFields = ReflectionHelper.getAllFields(refTable.getDataClass());

        Field sourceField = dataFields.get(key);
        sourceField.setAccessible(true);
        try {
            Object value = sourceField.get(d);
            Class<?> sourceClass = sourceField.getType();
//            ArrayList<HashMap<String, Object>> res = new ArrayList<>();
            QueryResult res = null;

            try {
                WhereClause wc = createWhereClause(refKey, sourceClass, value);
                Query q = new Query().from(refTable.getName()).select(refTextKey).where(wc);
                res = q.execute();
            } catch (TableNotFoundException | QueryException | FieldNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } // catch

            if (res.size() > 0) {
//            if (res.size() > 0) {
                Object replacementValue = res.getResults().get(0).get(refTextKey);
//                Object replacementValue = res.get(0).get(refTextKey);
                Field replacementField = dataFields.get(targetField);
                replacementField.setAccessible(true);
                replacementField.set(d, replacementValue);
            } // if refRecord...
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } // catch
    } // resolve

    private WhereClause createWhereClause(String refKey, Class<?> sourceClass, Object value) {
        WhereClause wc = null;

        if (sourceClass == int.class) {
            wc = new IntClause(refKey, "==", (int)value);
        }
        else if (sourceClass == float.class) {
            wc = new FloatClause(refKey, "==", (float)value);
        }
        else if (sourceClass == String.class) {
            wc = new StringClause(refKey, "==", (String)value);
        }

        return wc;
    } // createWhereClause

    @Override
    public String toString() {
        return String.format("\t\tReference: refTable=%s, key=%s, refKey=%s, refTextKey=%s, targetField=%s", refTable.getName(), key, refKey, refTextKey, targetField);
    } // toString
} // class Reference
