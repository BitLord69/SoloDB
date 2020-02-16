package com.janinc.util;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-11
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.Table;
import com.janinc.exceptions.DatabaseNotInitializedException;
import com.janinc.testapp.testdb.DiscDB;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public static Map<String, Method> getAllMethods(Class<?> theClass) {
        Map<String, java.lang.reflect.Method> methods = new HashMap<>();

        while (!(theClass.getName().equals(Object.class.getName()))) {
            Method[] ms = theClass.getDeclaredMethods();
            for (Method m: ms) {
                int mods = m.getModifiers();
                if (!Modifier.isStatic(mods) && !Modifier.isFinal(mods)) methods.put(m.getName(), m);
            } // for f...

            theClass = theClass.getSuperclass();
        } // while...

        return methods;
    } // getAllMethods

    public static Field getField(String table, DataObject d, String fieldName) throws DatabaseNotInitializedException {
        Table<? extends DataObject> t = DiscDB.getInstance().getTable(table);
        Map<String, Field> dataFields = getAllFields(t.getDataClass());
        return dataFields.get(fieldName);
    } // getField

    public static Method getMethod(Class<?> c, String methodName) throws DatabaseNotInitializedException {
        Map<String, Method> dataFields = getAllMethods(c);
        return dataFields.get(methodName);
    } // getField
    public static Object runGetter(Field field, DataObject d) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String name  = field.getName();
        name = "get" + name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        Method m = d.getClass().getDeclaredMethod(name);
        m.setAccessible(true);
        return m.invoke(d);
    } // runGetter

    public static Object getFieldValue(String table, DataObject d, String fieldName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, DatabaseNotInitializedException {
        return runGetter(getField(table, d, fieldName), d);
    } // getFieldValue
} // class ReflectionHelper
