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
public @interface StringField {
    String name() default "";
    int maxlength() default 0;

    boolean lookup() default false;
    String lookupTable() default "";
    String lookupField() default "";
    String shadowField() default "";

    boolean uniquevalue() default false;
    boolean usevalidation() default false;
}