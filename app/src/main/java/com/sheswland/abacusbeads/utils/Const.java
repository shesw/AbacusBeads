package com.sheswland.abacusbeads.utils;

import android.os.Environment;

import java.io.File;

public class Const {

    public static String DocumentPath;
    public static final String subFileName = "AbacusBeads";
    public static final String divide = "\t\t";

    static {
        DocumentPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
    }

    public static final String[] dayTableIncomeType = new String[] {"类型", "支出", "收入"};

    public enum Accuracy{
        day, month, year, all
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

    public static String getPrintPath(String subFile) {
        return DocumentPath + File.separator + subFile + File.separator;
    }

}
