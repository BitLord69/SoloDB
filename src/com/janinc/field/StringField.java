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
import com.janinc.query.Query;
import com.janinc.query.QueryException;
import com.janinc.query.clause.StringClause;
import com.janinc.query.clause.WhereClause;
import com.janinc.util.ReflectionHelper;
import com.janinc.exceptions.ValidationException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StringField<T> extends Field<T>{
    private int maxLength = 0;
    private boolean mandatory = false;
    private boolean useValidation = false;
    private boolean unique = false;

    public StringField(String name) {
        super(name, Type.STRING);
    }

    public StringField(String name, com.janinc.annotations.StringField annotation) {
        this(name);
        this.unique = annotation.uniquevalue();
        this.maxLength = annotation.maxlength();
        this.mandatory = annotation.mandatory();
        this.useValidation = maxLength > 0 || mandatory || unique;
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
    public void validate(DataObject d) throws ValidationException {
        if (useValidation)
        {
            Map<String, java.lang.reflect.Field> fields = ReflectionHelper.getAllFields(d.getClass());

            String value = "";
            try {
                java.lang.reflect.Field field = fields.get(getName());
                field.setAccessible(true);

                value = (String) field.get(d);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } // catch

            if (unique) {
                ArrayList<HashMap<String, Object>> res = new ArrayList<>();

                try {
                    WhereClause wc = new StringClause(getName(), "==", (String)value);
                    Query q = new Query().from(Database.getInstance().getTableName(d.getClass())).select(getName()).where(wc);
                    res = q.execute();
                } catch (TableNotFoundException | QueryException | FieldNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                } // catch

                if (res.size() > 0)
                    throw new ValidationException(getName(), String.format("field is unique, and the value [%s] has already been entered!", value));
            } // if unique...

            if (mandatory && (value.isEmpty() || value.isBlank()))
                throw new ValidationException(getName(), "field is mandatory, please provide a string!");

            if (maxLength > 0 && value.length() > maxLength) {
                throw new ValidationException(getName(), "field too long, only [" + maxLength +  "] characters allowed!");
            } // if value...
        } // if useValidation...
    } // validate

    @Override
    public String toString() {
        return String.format("StringField - name: %s, maxLength: %d, mandatory: %b, useValidation: %b, unique: %b", getName(), maxLength, mandatory, useValidation, unique);
    } // toString
} // class StringField
