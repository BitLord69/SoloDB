package com.janinc.util;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-11
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ReflectionHelper {
    public static Map<String, Field> getAllFields(Class<?> theClass) {
        Map<String, java.lang.reflect.Field> fields = new HashMap<>();

        while (!(theClass.getName().equals(Object.class.getName()))) {
            java.lang.reflect.Field[] fs = theClass.getDeclaredFields();
            for (java.lang.reflect.Field f: fs) {
                int mods = f.getModifiers();
                if (!Modifier.isStatic(mods) && !Modifier.isFinal(mods)) fields.put(f.getName(), f);
            } // for f...

            theClass = theClass.getSuperclass();
        } // while...

        return fields;
    } // getAllFields
} // class ReflectionHelper
