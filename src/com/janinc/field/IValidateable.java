package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-31
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/
@FunctionalInterface
public interface IValidateable {
    <T> boolean validate(T t);
}
