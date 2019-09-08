package com.sheswland.abacusbeads.database.tables;

import com.sheswland.abacusbeads.database.database_interface.Table;

import org.litepal.crud.LitePalSupport;

public class AccountDayTable extends LitePalSupport implements Table {

    private String table_id;

    private String date;
    private int year;
    private int month;
    private int day;
    private String content;
    private boolean isIncome;
    private float spend;
    private float remain;

    public String getTable_id() {
        return table_id;
    }

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

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }

    public float getSpend() {
        return spend;
    }

    public void setSpend(float spend) {
        this.spend = spend;
    }

    public float getRemain() {
        return remain;
    }

    public void setRemain(float remain) {
        this.remain = remain;
    }
}
