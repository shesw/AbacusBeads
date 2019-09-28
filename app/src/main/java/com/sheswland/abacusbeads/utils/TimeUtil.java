package com.sheswland.abacusbeads.utils;

import com.sheswland.abacusbeads.database.database_interface.Table;
import com.sheswland.abacusbeads.database.tables.OperateDataTable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static long getTimeStamp(int[] time) {
        Date date = getDate(time);
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.getTimeInMillis();
        }
        return -1;
    }

    public static Date getDate(int[] time) {
        int year = -1, month = -1, day = -1;
        int hour = 0, minute = 0, second = 0;
        if (time.length >= 1) year = time[0];
        if (time.length >= 2) month = time[1];
        if (time.length >= 3) day = time[2];
        if (time.length >= 4) hour = time[3];
        if (time.length >= 5) minute = time[4];
        if (time.length >= 6) second = time[5];
        if (year == -1 || month == -1 || day == -1) return null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        try {
            Date date = df.parse(year + "-"
                    + TextUtil.formatNumber2xx(month) + "-"
                    + TextUtil.formatNumber2xx(day) + "-"
                    + TextUtil.formatNumber2xx(hour) + "-"
                    + TextUtil.formatNumber2xx(minute) + "-"
                    + TextUtil.formatNumber2xx(second)
            );
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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

    public static int[] getYMD(Date date) {
        int[] res = new int[3];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        res[0] = calendar.get(Calendar.YEAR);
        res[1] = calendar.get(Calendar.MONTH) + 1;
        res[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return res;
    }

    public static int[] getHMS(Date date) {
        int[] res = new int[3];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        res[0] = calendar.get(Calendar.HOUR);
        res[1] = calendar.get(Calendar.MINUTE);
        res[2] = calendar.get(Calendar.SECOND);
        return res;
    }
}
