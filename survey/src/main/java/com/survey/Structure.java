package com.survey;

import oracle.jrockit.jfr.events.Bits;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @ClassName: Structure
 * @Description: date structure
 * @Author: Ian
 * @Date: 2018/3/5 15:35
 * @Version: 1.0
 */
public class Structure {

    public static void main(String[] args) {
        /**
         * enumeration
         */
        Enumeration<String> enumeration ;
        Vector<String> dayNames = new Vector<String>();
        dayNames.add("Sunday");
        dayNames.add("Monday");
        enumeration = dayNames.elements();
        while (enumeration.hasMoreElements()){
            System.out.println(enumeration.nextElement());
        }
        /**
         * BitSet
         */
        BitSet bits1 = new BitSet(16);
        BitSet bits2 = new BitSet(16);
        // set some bits
        for(int i=0; i<30; i++) {
            if((i%2) == 0) bits1.set(i);
            if((i%5) != 0) bits2.set(i);
        }
        System.out.println("Initial pattern in bits1: ");
        System.out.println(bits1);
        System.out.println("\nInitial pattern in bits2: ");
        System.out.println(bits2);

        // AND bits
        bits2.and(bits1);
        System.out.println("\nbits2 AND bits1: ");
        System.out.println(bits2);

        // OR bits
        bits2.or(bits1);
        System.out.println("\nbits2 OR bits1: ");
        System.out.println(bits2);

        // XOR bits
        bits2.xor(bits1);
        System.out.println("\nbits2 XOR bits1: ");
        System.out.println(bits2);

    }
}
