package com.makkras.shop.entity;

public enum PeriodType {
    DAILY ("DAILY"),
    WEEKLY ("WEEKLY"),
    MONTHLY ("MONTHLY"),
    YEARLY ("YEARLY");

    private final String name;

    PeriodType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
