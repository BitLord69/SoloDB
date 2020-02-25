package com.janinc.exceptions;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.util.TextUtil;

public class TableNotFoundException extends RuntimeException {

    public TableNotFoundException(String table) {
        super(String.format("%sTable '%s' not found!", TextUtil.pimpString("query.from error: ", TextUtil.LEVEL_WARNING), TextUtil.pimpString(table, TextUtil.LEVEL_INFO)));
    } // TableNotFoundException
} // class TableNotFoundException
