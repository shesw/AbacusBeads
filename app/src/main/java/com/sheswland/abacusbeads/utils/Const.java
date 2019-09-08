package com.sheswland.abacusbeads.utils;

public class Const {

    public static final String[] dayTableIncomeType = new String[] {"类型", "支出", "收入"};

    public enum Accuracy{
        day, month, year
    }

    public final static String OPERATE_TABLE_PREFIX = "operate_tab_";

    public enum TableType {
        OPERATE_TAB,
        ACCOUNT_DAY,
        ACCOUNT_MONTH_AND_YEAR
    }

    public enum FilterAccuracy {
        all_month,
        all_year,
        year,
        month,
        day,
        hour,
        minute,
        second,
    }

}
