package com.janinc.util;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-25
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import java.security.SecureRandom;

public class RandomEnum {
    private static final SecureRandom random = new SecureRandom();

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    } // randomEnum
} // RandomEnum
