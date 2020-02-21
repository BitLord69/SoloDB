package com.janinc.exceptions;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.util.TextUtil;

public class ValidationException extends Exception {

    public ValidationException(String field, String message) {
        super(String.format("%s '%s': %s", TextUtil.pimpString("Validation error for field", TextUtil.LEVEL_WARNING),
                TextUtil.pimpString(field, TextUtil.LEVEL_STRESSED),
                TextUtil.pimpString(message, TextUtil.LEVEL_INFO)));
    } // DBNotInitializedException
} // class DBNotInitializedException

