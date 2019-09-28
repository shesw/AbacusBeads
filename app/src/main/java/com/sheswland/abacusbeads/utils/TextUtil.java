package com.sheswland.abacusbeads.utils;

import com.sheswland.abacusbeads.database.database_interface.Table;
import com.sheswland.abacusbeads.database.tables.OperateDataTable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TextUtil {
    public static boolean isEmpty(String string) {
        return "".equals(string) || null == string;
    }

    public static String formatNumber2xx(int i) {
        return i > 9 ? String.valueOf(i) : "0" + i;
    }

    public static Float formatFloat2(float f) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return Float.valueOf(decimalFormat.format(f));
    }

    public static String getExtensionWithDot(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

}
