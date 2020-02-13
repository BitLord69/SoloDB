package com.janinc;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-04
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.annotations.StringField;
import com.janinc.util.FileHandler;
import com.janinc.util.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public abstract class DataObject implements java.io.Serializable {
    public static final long serialVersionUID = 42424242L;

    @StringField(name = "id", uniquevalue = true)
    private String id = "";

    public String getId() { return id; }
    protected  void setId(String id) { this.id = id; }

    public void copy(DataObject d) {
        Map<String, Field> sourceFields = ReflectionHelper.getAllFields(d.getClass());
        Map<String, Field> targetFields = ReflectionHelper.getAllFields(getClass());

        sourceFields.forEach((k, v)-> {
            v.setAccessible(true);
            targetFields.get(k).setAccessible(true);
            try {
                if (!Modifier.isTransient(v.getModifiers()))
                    targetFields.get(k).set(this, v.get(d));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } // catch
        });
    } // copy

    public void refresh(){
        copy((DataObject) FileHandler.readFile("", getId()));
    } // refresh
} // class DataObject
