package com.technology.nerve;

public class Abca {
    public static void main(String[] args) {
        int value = 1000;
        System.out.println(Integer.toString(value,2));
        value >>>= 4;
        System.out.println(Integer.toString(value,2));

        System.out.println(value);
    }
}
