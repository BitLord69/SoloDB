package com.janinc.util;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-20
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public class TextUtil {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final int LEVEL_NORMAL = 0;
    public static final int LEVEL_BOLD = 1;
    public static final int LEVEL_STRESSED = 2;
    public static final int LEVEL_INFO = 3;
    public static final int LEVEL_WARNING = 4;

    public static String pimpString(String title, int level) {
        String prefix;

        switch (level) {
            case LEVEL_NORMAL:
                prefix = ANSI_BLACK;
                break;

            case LEVEL_BOLD:
                prefix = ANSI_YELLOW;
                break;

            case LEVEL_STRESSED:
                prefix = ANSI_GREEN;
                break;

            case LEVEL_INFO:
                prefix = ANSI_BLUE;
                break;

            case LEVEL_WARNING:
                prefix = ANSI_RED;
                break;

            default:
                prefix = "";
        } // switch

        return prefix + title + ANSI_RESET;
    } // pimpString

    public static String pimpString(int number, int level) {
        return pimpString(String.format("%d", number), level);
    } // pimpString

    public static String pimpString(double number, int level) {
        return pimpString(String.format("%.2f", number), level);
    } // pimpString

}
