package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.exceptions.ValidationException;
import com.janinc.util.ReflectionHelper;
import com.janinc.util.TextUtil;

import java.lang.reflect.InvocationTargetException;

public class FloatField<T> extends Field<T> {

    private float minValue = Float.MIN_VALUE;
    private float maxValue = Float.MAX_VALUE;

    public FloatField(String name) {
        super(name);
    }

    public FloatField(String name, com.janinc.annotations.FloatField annotation) {
        this(name);
        minValue = annotation.minvalue();
        maxValue = annotation.maxvalue();
    }

    @Override
    public void validate(DataObject d) throws ValidationException, IllegalAccessException, InvocationTargetException {
        float value = (float) ReflectionHelper.getFieldValue(d, getName());

        if (value < minValue || value > maxValue) {
            throw new ValidationException(getName(), String.format("value has to be within [%s] and [%s], but is %s!",
                    TextUtil.pimpString(minValue, TextUtil.LEVEL_INFO),
                    TextUtil.pimpString(maxValue, TextUtil.LEVEL_INFO),
                    TextUtil.pimpString(value, TextUtil.LEVEL_STRESSED)));
        } // if value...
    } // validate

    @Override
    public void updateDirtyField(DataObject d) {
        float value = 0.0f;
        try {
            value = (float)ReflectionHelper.getFieldValue(d, getName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }  // catch

        d.setDirtyValue(getName(), value);
    } // updateDirtyField

    @Override
    public String toString() {
        return String.format("FloatField - name: '%s', minValue: %.2f, maxValue: %.2f", getName(), minValue, maxValue);
    } // toString
} // class FloatField
