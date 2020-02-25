package com.janinc.testapp.testdb;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-25
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public enum Colors {
    WHITE ("white"),
    YELLOW ("yellow"),
    GREEN ("green"),
    RED ("red"),
    ORANGE ("orange"),
    PURPLE ("purple");

    private String color;

    Colors(String color) { this.color = color; }
    public String getColor() { return color; }
} // enum Colors
