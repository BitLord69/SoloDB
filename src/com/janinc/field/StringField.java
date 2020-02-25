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

    @Override
    public void validate(DataObject d) throws ValidationException, InvocationTargetException, IllegalAccessException {
        String value = (String) ReflectionHelper.getFieldValue(d, getName());

        if (mandatory && (value == null || value.isEmpty() || value.isBlank()))
            throw new ValidationException(getName(), "field is mandatory, please provide a value!");

        if (maxLength > 0 && value.length() > maxLength) {
            throw new ValidationException(getName(), String.format("entered string is too long, only [%s] characters allowed!", TextUtil.pimpString(maxLength, TextUtil.LEVEL_STRESSED)));
        } // if value...

        String dirtyValue = d.getDirtyValue(getName()) == null ? "" : (String) d.getDirtyValue(getName());
        if (unique && !dirtyValue.equals(value)) {
            QueryResult res = null;
            try {
                // Set the old value temporarily, so we won't get the same record back in the search result
                ReflectionHelper.setFieldValue(d, getName(), dirtyValue);

                WhereClause wc = new StringClause(getName(), "==", (String)value);
                Query q = new Query().
                        from(Database.getInstance().getTableName(d.getClass())).
                        select(getName()).
                        where(wc);
                res = q.execute();

                // Put the proper/actual value back
                ReflectionHelper.setFieldValue(d, getName(), value);
            } catch (TableNotFoundException | QueryException | FieldNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } // catch

            if (res.size() > 0) {
                throw new ValidationException(getName(),
                        String.format("field is unique, and the value [%s] has already been entered!",
                                TextUtil.pimpString(value, TextUtil.LEVEL_STRESSED)));
            } // if res...
        } // if unique...
    } // validate

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
    public boolean isUnique() {
        return unique;
    } // isUnique

    @Override
    public String toString() {
        return String.format("StringField - name: '%s', maxLength: %d, mandatory: %b, unique: %b", getName(), maxLength, mandatory, unique);
    } // toString
} // class StringField
