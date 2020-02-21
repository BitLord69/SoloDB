package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.Database;
import com.janinc.DataObject;
import com.janinc.exceptions.*;
import com.janinc.query.*;
import com.janinc.query.clause.*;
import com.janinc.util.ReflectionHelper;
import com.janinc.util.TextUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class StringField<T> extends Field<T> {
    private int maxLength = 0;
    private boolean mandatory = false;
    private boolean unique = false;

    public StringField(String name) {
        super(name, Type.STRING);
    }

    public StringField(String name, com.janinc.annotations.StringField annotation) {
        this(name);
        this.unique = annotation.unique();
        this.maxLength = annotation.maxlength();
        this.mandatory = annotation.mandatory();
    }

    public int getMaxLength() {
        return maxLength;
    }
    public boolean isMandatory() {
        return mandatory;
    }
    public boolean isUnique() {
        return unique;
    }

    @Override
    public void validate(DataObject d) throws ValidationException, InvocationTargetException, IllegalAccessException {
        String value = (String) ReflectionHelper.getFieldValue(d, getName());

        if (mandatory && (value == null || value.isEmpty() || value.isBlank()))
            throw new ValidationException(getName(), "field is mandatory, please provide a value!");

        if (maxLength > 0 && value.length() > maxLength) {
            throw new ValidationException(getName(), "field too long, only [" + maxLength +  "] characters allowed!");
        } // if value...

        String dirtyValue = d.getDirtyValue(getName()) == null ? "" : (String) d.getDirtyValue(getName());
        if (unique && !dirtyValue.equals(value)) {
            ArrayList<HashMap<String, Object>> res = new ArrayList<>();

            try {
                // Set the old value temporarily, so we won't get the same record back in the search result
                String tempValue = value;
                ReflectionHelper.setFieldValue(d, getName(), dirtyValue);

                WhereClause wc = new StringClause(getName(), "==", (String)value);
                Query q = new Query().from(Database.getInstance().getTableName(d.getClass())).select(getName()).where(wc);
                res = q.execute();

                // Put the proper/actual value back
                ReflectionHelper.setFieldValue(d, getName(), value);
            } catch (TableNotFoundException | QueryException | FieldNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } // catch

            if (res.size() > 0) {
                throw new ValidationException(getName(), String.format("field is unique, and the value [%s] has already been entered!", TextUtil.pimpString(value, TextUtil.LEVEL_STRESSED)));
            } // if res...
        } // if unique...
    } // validate

    @Override
    public String toString() {
        return String.format("StringField - name: '%s', maxLength: %d, mandatory: %b, unique: %b", getName(), maxLength, mandatory, unique);
    } // toString

    @Override
    public void updateDirtyField(DataObject d) {
        if (!unique) return;

        String value = "";
        try {
            value = (String)ReflectionHelper.getFieldValue(d, getName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }  // catch

        d.setDirtyValue(getName(), value);
    } // updateDirtyField

    @Override
    public boolean isUniqueField() {
        return unique;
    } // isUniqueField
} // class StringField
