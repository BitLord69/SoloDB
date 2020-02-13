package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Field<T> implements IValidateable {
    private String name;
    private Type type;

    public Field(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' + ", type=" + type;
    }
//    public abstract boolean validate(T t);
} // class Field
