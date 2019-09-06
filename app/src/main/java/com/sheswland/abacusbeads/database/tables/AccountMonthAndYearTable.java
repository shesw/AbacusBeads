package com.sheswland.abacusbeads.database.tables;

import com.sheswland.abacusbeads.database.database_interface.Table;

import org.litepal.crud.LitePalSupport;

public class AccountMonthAndYearTable extends LitePalSupport implements Table {

    private String table_id;

    private String date;
    private float spend;
    private float income;
    private float remain;

    public String getTable_id() {
        return table_id;
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

    public float getSpend() {
        return spend;
    }

    public void setSpend(float spend) {
        this.spend = spend;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public float getRemain() {
        return remain;
    }

    public void setRemain(float remain) {
        this.remain = remain;
    }
}
