package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.exceptions.ValidationException;

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
    public boolean validate(DataObject d) throws ValidationException {
        if (useValidation)
        {
            String value = "";
            try {
                java.lang.reflect.Field field = d.getClass().getDeclaredField(getName());
                field.setAccessible(true);
                value = (String) field.get(d);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            // TODO: 2020-02-06 Handle check if field is unique... I.e. fix query engine first
            if (unique) {
                ;
            }

            if (mandatory && (value.isEmpty() || value.isBlank()))
                throw new ValidationException(getName(), "Field is mandatory, please provide a string!");

            if (value.length() > maxLength) {
                throw new ValidationException(getName(), "Field too long, only " + maxLength +  " characters allowed!");
            } // if value...

            return true;
        } // if useValidation...

        return true;
    } // validate
} // class StringField
