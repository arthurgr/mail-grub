package com.mailgrub.model;

public enum MeasurementType {
    KG("kg"),
    LB("lbs"),
    OZ("oz");

    private String value;

    MeasurementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
