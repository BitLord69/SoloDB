package com.janinc.annotations;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-06
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import java.util.function.BiConsumer;

public class AnnotationHandlerParams {
    private Class<?> fieldClass;

    private BiConsumer handler;

    public AnnotationHandlerParams() {
    }

    public Class getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(Class fieldClass) {
        this.fieldClass = fieldClass;
    }

    public BiConsumer getHandler() {
        return handler;
    }
    public void setHandler(BiConsumer handler) {
        this.handler = handler;
    }
} // AnnotationHandlerParams
