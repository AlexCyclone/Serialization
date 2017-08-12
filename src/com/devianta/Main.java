package com.devianta;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        ClassA a = fillA();
        ClassB b = fillB();
        b.setCla(a);

        System.out.println("Before:");
        System.out.println(a);
        System.out.println(b);
        System.out.println();

        Serializator srlA = new Serializator(new File("ahere.txt"));
        Serializator srlB = new Serializator(new File("bhere.txt"));
        srlA.serialize(a);
        srlB.serialize(b);

        ClassA dsrA = (ClassA) srlA.deserialize(new ClassA());
        ClassB dsrB = (ClassB) srlB.deserialize(new ClassB());

        System.out.println("After:");
        System.out.println(dsrA);
        System.out.println(dsrB);
    }

    public static ClassA fillA(){
        ClassA a = new ClassA();
        a.setBoolValue(true);
        a.setDoubleValue(1.11);
        a.setIntValue(111);
        a.setStringValue("AA");
        return a;
    }

    public static ClassB fillB(){
        ClassB b = new ClassB();
        b.setIntValueB(2);
        b.setStringValueB("BB");
        b.setBoolValue(false);
        b.setDoubleValue(2.22);
        b.setIntValue(222);
        b.setStringValue("BB");
        return b;
    }
}
