package com.janinc;

import com.janinc.testapp.TestApp;

import java.util.List;

public class Main {

    public static void main(String... args) {
        List.of(TestApp.class.getPackage().getClass().getClasses()).forEach(System.out::println);
	// write your code here
    }
}
