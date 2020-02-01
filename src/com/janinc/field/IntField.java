package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public class IntField<T> extends Field<T>{

    private int minValue = 0;
    private int maxValue = 0;
    private boolean useMinMaxValue = false;
//    private boolean useMaxValue = false;

    public IntField(String name) {
        super(name, Type.INT);
    }

    public IntField(String name, int minValue, int maxValue) {
        this(name);
        this.minValue = minValue;
        this.maxValue = maxValue;
        useMinMaxValue = true;
    }

    @Override
    public <T> boolean validate(T t) {
        return false;
    } // validate
} // class StringField
