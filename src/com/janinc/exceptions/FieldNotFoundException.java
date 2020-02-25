package com.janinc.exceptions;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.util.TextUtil;

public class FieldNotFoundException extends RuntimeException {

    public FieldNotFoundException(String fieldName) {
        super(String.format("Field '%s' not found!", TextUtil.pimpString(fieldName, TextUtil.LEVEL_STRESSED)));
    } // TableNotFoundException
} // class TableNotFoundException

