package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.util.ReflectionHelper;
import com.janinc.exceptions.ValidationException;

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

            // TODO: 2020-02-06 Handle check if field is unique... I.e. fix query engine first
            if (unique) {
                ;
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
        return String.format("StringField - maxLength: %d, mandatory: %b, useValidation: %b, unique: %b", maxLength, mandatory, useValidation, unique);
    } // toString
} // class StringField
