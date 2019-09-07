package com.sheswland.abacusbeads.utils;

import com.sheswland.abacusbeads.database.database_interface.Table;
import com.sheswland.abacusbeads.database.tables.OperateDataTable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextUtil {
    public static boolean isEmpty(String string) {
        return "".equals(string) || null == string;
    }

    public static String formatDate2yyyyMMdd(Table table) {
        if (table instanceof OperateDataTable) {
            OperateDataTable table1 = (OperateDataTable) table;
            return table1.getYear() + "" + (table1.getMonth() > 9 ? table1.getMonth() + "" : "0" + table1.getMonth()) + "" + (table1.getDay() > 9 ? table1.getDay() + "" : "0" + table1.getDay());
        }
        return "20190905";
    }

    public static String formatDate2yyyyMM(Table table) {
        if (table instanceof OperateDataTable) {
            OperateDataTable table1 = (OperateDataTable) table;
            return table1.getYear() + "" + (table1.getMonth() > 9 ? table1.getMonth() + "" : "0" + table1.getMonth());
        }
        return "201909";
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String timeString = format.format(date);
        return timeString;
    }
}
