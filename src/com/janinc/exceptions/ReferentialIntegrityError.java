package com.janinc.exceptions;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-25
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.Database;
import com.janinc.Table;
import com.janinc.util.TextUtil;

import java.util.stream.Collectors;

public class ReferentialIntegrityError extends RuntimeException {
    public ReferentialIntegrityError(String table, DataObject d) {
        super(String.format("Record [%s] in table [%s] has dependent records in one or more of the tables: %s",
                TextUtil.pimpString(d.getId(), TextUtil.LEVEL_WARNING),
                TextUtil.pimpString(table, TextUtil.LEVEL_INFO),
                Database.getInstance()
                        .getTable(table)
                        .getReferenceTo()
                        .stream()
                        .map(Table::getName)
                        .collect(Collectors.joining(", "))));
    } // ReferentialIntegrityError
} // class ReferentialIntegrityError
