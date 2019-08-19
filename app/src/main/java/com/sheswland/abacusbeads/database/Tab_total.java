package com.sheswland.abacusbeads.database;

import org.litepal.crud.LitePalSupport;

public class Tab_total extends LitePalSupport {
    public static final int EXPENDITURE = 0;
    public static final int INCOME = 1;
    private int year = 2019;
    private int month = 6;
    private int day = 9;
    private int type = EXPENDITURE;
    private float value = 0.0f;
    private String description;
    private float remain = 0.0f;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRemain() {
        return remain;
    }

    public void setRemain(float remain) {
        this.remain = remain;
    }
}
