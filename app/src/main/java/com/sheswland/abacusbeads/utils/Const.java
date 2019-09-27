package com.sheswland.abacusbeads.utils;

import android.os.Environment;

import java.io.File;

public class Const {

    /******************************* table const ********************/
    public static String DocumentPath;
    public static final String subFileName = "AbacusBeads";
    public static final String dayTableSubFile = "day_table";
    public static final String monthTableSubFile = "month_table";
    public static final String yearTableSubFile = "year_table";
    public static final String divide = "              ";

    public static final String[] dayTableIncomeType = new String[] {"类型", "支出", "收入"};

    // 专门为查询页的查询表转换而使用，其它部分请使用FilterAccuracy;
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
        timestamp
    }
    /******************************* table const end ********************/

    /************************* file const *****************************/
    static {
        DocumentPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
    }

    public static String getPrintPath(String subFile) {
        return DocumentPath + File.separator + subFile + File.separator;
    }
    /************************* file const end *****************************/

    /**************************** pf const ****************************/
    public static String PF_NANE = "AbacusBeads";
    public static String PF_INIT_HISTORY_DATA = "pf_init_history_data";

    /********************************pf const end ********************/
}
