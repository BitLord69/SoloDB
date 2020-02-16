package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.Database;
import com.janinc.exceptions.FieldNotFoundException;
import com.janinc.exceptions.TableNotFoundException;
import com.janinc.exceptions.ValidationException;
import com.janinc.query.Query;
import com.janinc.query.QueryException;
import com.janinc.query.clause.IntClause;
import com.janinc.query.clause.StringClause;
import com.janinc.query.clause.WhereClause;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class IntField<D> extends Field<D>{
    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;
    private boolean useValidation = false;
    private boolean unique = false;

    public IntField(String name) {
        super(name, Type.INT);
    }

    public IntField(String name, com.janinc.annotations.IntField annotation) {
        this(name);
        minValue = annotation.minvalue();
        maxValue = annotation.maxvalue();
        useValidation = annotation.useValidation();
        unique = annotation.uniqueValue();
    }

    protected boolean greaterThan(Object op1, Object op2) { return (int)op1 > (int)op2; } // greaterThan
    protected boolean smallerThan(Object op1, Object op2) { return (int)op1 < (int)op2; } // smallerThan

    @Override
    public void validate(DataObject d) throws ValidationException {
        int value = Integer.MIN_VALUE;
        if (useValidation) {
            try {
                java.lang.reflect.Field field = d.getClass().getDeclaredField(getName());
                field.setAccessible(true);
                value = (int) field.get(d);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            } // catch

            if (unique) {
                ArrayList<HashMap<String, Object>> res = new ArrayList<>();

                try {
                    WhereClause wc = new IntClause(getName(), "==", (int)value);
                    Query q = new Query().from(Database.getInstance().getTableName(d.getClass())).select(getName()).where(wc);
                    res = q.execute();
                } catch (TableNotFoundException | QueryException | FieldNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                } // catch

                if (res.size() > 0)
                    throw new ValidationException(getName(), String.format("field is unique, and the value [%d] has already been entered!", value));
            } // if unique...

            if (value < minValue || value > maxValue) {
                throw new ValidationException(getName(), String.format("value has to be within [%d] and [%d], but is %d!", minValue, maxValue, value));
            } // if value...
        } // if useValidation...
    } // validate
    
    @Override
    public String toString() {
        return String.format("IntField - minValue: %d, maxValue: %d, useValidation: %b, unique: %b", minValue, maxValue, useValidation, unique);
    } // toString
} // class StringField
