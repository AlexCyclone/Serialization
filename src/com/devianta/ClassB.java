package com.devianta;

public class ClassB extends ClassA {

    @Save
    private int intValueB;
    @Save
    private String stringValueB;
    @Save
    private ClassA cla = new ClassA();

    public ClassB() {
    }

    public void setIntValueB(int intValueB) {
        this.intValueB = intValueB;
    }

    public void setStringValueB(String stringValueB) {
        this.stringValueB = stringValueB;
    }

    public void setCla(ClassA cla) {
        this.cla = cla;
    }

    public int getIntValueB() {
        return intValueB;
    }

    public String getStringValueB() {
        return stringValueB;
    }

    public ClassA getCla() {
        return cla;
    }

    @Override
    public String toString() {
        return "ClassB [intValue=" + getIntValue()
                + ", doubleValue=" + getDoubleValue() + ", stringValue=" + getStringValue() + ", boolValue="
                + isBoolValue() + ", intValueB=" + intValueB + ", stringValueB=" + stringValueB + ", cla=" + cla + "]";
    }

}
