package com.janinc.annotations;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-03
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntField {
    String name() default "";
    int minvalue() default Integer.MIN_VALUE;
    int maxvalue() default Integer.MAX_VALUE;
    boolean uniqueValue() default false;
    boolean useValidation() default false;
}