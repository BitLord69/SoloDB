package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.exceptions.ValidationException;

public class FloatField<T> extends Field<T> {

    private float minValue = Float.MIN_VALUE;
    private float maxValue = Float.MAX_VALUE;
    private boolean useValidation = false;
//    private boolean useMaxValue = false;

    public FloatField(String name) {
        super(name, Type.FLOAT);
    }

    public FloatField(String name, com.janinc.annotations.FloatField annotation) {
        this(name);
        minValue = annotation.minvalue();
        maxValue = annotation.maxvalue();
        useValidation = annotation.useValidation();
    }

    @Override
    public void validate(DataObject d) throws ValidationException {
        float value = Float.MIN_VALUE;
        if (useValidation)
        {
            try {
                java.lang.reflect.Field field = d.getClass().getDeclaredField(getName());
                field.setAccessible(true);
                value = (float) field.get(d);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if (value < minValue || value > maxValue) {
                throw new ValidationException(getName(), toString());
            }
        } // if useValidation...
    } // validate
} // class StringField
