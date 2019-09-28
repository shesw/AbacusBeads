package com.sheswland.abacusbeads.database.tables;

import com.sheswland.abacusbeads.database.database_interface.Table;
import com.sheswland.abacusbeads.utils.DebugLog;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class OperateDataTable extends LitePalSupport implements Table {
    private final String TAG = "OperateDataTable";
    private String table_id;

    private boolean isIncome;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private String content;
    private float spend;
    private float remain;
    private Date date;

    public void debugLogTable() {
        DebugLog.d(TAG, "table_id " + table_id + "\n" +
                "isIncome " + isIncome + "\n" +
                "year " + year + "\n" +
                "month " + month + "\n" +
                "day " + day + "\n" +
                "hour " + hour + "\n" +
                "minute " + minute + "\n" +
                "second " + second + "\n" +
                "content " + content + "\n" +
                "spend " + spend + "\n" +
                "remain " + remain + "\n");
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public float getRemain() {
        return remain;
    }

    public void setRemain(float remain) {
        this.remain = remain;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
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

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getSpend() {
        return spend;
    }

    public void setSpend(float spend) {
        this.spend = spend;
    }
}
