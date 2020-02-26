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

public abstract class Field<T> implements IValidateable, IUniqueField {
    private String name;

    public Field(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "name='" + name;
    }
    public boolean isUnique() {
        return false;
    } // isUniqueField
} // class Field
