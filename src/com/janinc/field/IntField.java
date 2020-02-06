package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-01-30
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.exceptions.ValidationException;

public class IntField<D> extends Field<D>{

    private int minValue = 0;
    private int maxValue = 0;
    private boolean useValidation = false;
    private boolean unique = false;

    public IntField(String name) {
        super(name, Type.INT);
    }

    public IntField(String name, int minValue, int maxValue, boolean useValidation, boolean unique) {
        this(name);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.useValidation = useValidation;
        this.unique = unique;
    }

    @Override
    public boolean validate(DataObject d) throws ValidationException {
        int value = Integer.MIN_VALUE;
        if (useValidation)
        {
            // TODO: 2020-02-06 Handle check if field is unique... I.e. fix query engine first  
            try {
                java.lang.reflect.Field field = d.getClass().getDeclaredField(getName());
                field.setAccessible(true);
                value = (int) field.get(d);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if (value < minValue || value > maxValue) {
                throw new ValidationException(getName(), this.toString());
            }

            return true;
        } // if useValidation...
        
        return true;
    } // validate
    
    @Override
    public String toString() {
        return "IntField{" +
                "minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", useValidation=" + useValidation +
                ", unique=" + unique +
                '}';
    }
} // class StringField
