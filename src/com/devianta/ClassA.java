package com.devianta;

public class ClassA {
    @Save
    private int intValue;
    @Save
    private double doubleValue;
    @Save
    private String stringValue;
    @Save
    private boolean boolValue;

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public boolean isBoolValue() {
        return boolValue;
    }

    @Override
    public String toString() {
        return "ClassA [intValue=" + intValue + ", doubleValue=" + doubleValue + ", stringValue=" + stringValue
                + ", boolValue=" + boolValue + "]";
    }


}
