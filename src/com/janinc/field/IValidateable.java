package com.janinc.field;

import com.janinc.DataObject;
import com.janinc.exceptions.ValidationException;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-31
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/
@FunctionalInterface
public interface IValidateable {
    void validate(DataObject d) throws ValidationException;
}
