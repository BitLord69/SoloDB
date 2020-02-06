package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;

public class FloatField<T> extends Field<T> {

    private float minValue = 0;
    private float maxValue = 0;
    private boolean useMinMaxValue = false;
//    private boolean useMaxValue = false;

    public FloatField(String name) {
        super(name, Type.FLOAT);
    }

    public FloatField(String name, float minValue, float maxValue) {
        this(name);
        this.minValue = minValue;
        this.maxValue = maxValue;
        useMinMaxValue = true;
    }

    @Override
    public boolean validate(DataObject data) {
        return false;
    } // validate
} // class StringField
