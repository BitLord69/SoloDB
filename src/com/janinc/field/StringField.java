package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public class StringField<T> extends Field<T>{
    private int maxLength = 0;

    public StringField(String name) {
        super(name, Type.STRING);
    }

    public StringField(String name, int maxLength){
        this(name);
        this.maxLength = maxLength;
    }

    @Override
    public <T> boolean validate(T t) {
        return false;
    } // validate
} // class StringField
