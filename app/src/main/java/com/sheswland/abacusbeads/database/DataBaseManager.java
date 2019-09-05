package com.sheswland.abacusbeads.database;

import com.sheswland.abacusbeads.database.database_interface.Table;
import com.sheswland.abacusbeads.database.tables.OperateDataTable;
import com.sheswland.abacusbeads.utils.DebugLog;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataBaseManager {

    private final static String TAG = "DataBaseManager";

    public enum TableType {
        OPERATE_TAB
    }

    public enum FilterAccuracy {
        year,
        month,
        day,
        hour,
        minute,
        second,
    }

    private static DataBaseManager _Holder;

    /********************** static methods ***********************/
    public static DataBaseManager getInstance() {
        if (_Holder == null) {
            _Holder = new DataBaseManager();
        }
        return _Holder;
    }

    public static Table produceTable(TableType type, Date date, Table originTable) {
        switch (type) {
            case OPERATE_TAB:
                String tableId = getTabeId(type, date, FilterAccuracy.month);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                if (originTable instanceof OperateDataTable) {
                    ((OperateDataTable)originTable).setTableId(tableId);
                    ((OperateDataTable)originTable).setYear(calendar.get(Calendar.YEAR));
                    ((OperateDataTable)originTable).setMonth(calendar.get(Calendar.MONTH) + 1);
                    ((OperateDataTable)originTable).setDay(calendar.get(Calendar.DAY_OF_MONTH));
                    ((OperateDataTable)originTable).setHour(calendar.get(Calendar.HOUR));
                    ((OperateDataTable)originTable).setMinute(calendar.get(Calendar.MINUTE));
                    ((OperateDataTable)originTable).setSecond(calendar.get(Calendar.SECOND));
                    return originTable;
                } else {
                    OperateDataTable operateData = new OperateDataTable();
                    operateData.setTableId(tableId);
                    operateData.setYear(calendar.get(Calendar.YEAR));
                    operateData.setMonth(calendar.get(Calendar.MONTH) + 1);
                    operateData.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                    operateData.setHour(calendar.get(Calendar.HOUR));
                    operateData.setMinute(calendar.get(Calendar.MINUTE));
                    operateData.setSecond(calendar.get(Calendar.SECOND));
                    return operateData;
                }
            default:
                break;

        }
        return null;
    }

    public static String getTabeId(TableType type, Date date, FilterAccuracy accuracy) {
        String pattern = "yyyyMM";
        switch (accuracy) {
            case second:
                pattern = "yyyyMMddHHmmss";
                break;
            case minute:
                pattern = "yyyyMMddHHmm";
                break;
            case hour:
                pattern = "yyyyMMddHH";
                break;
            case day:
                pattern = "yyyyMMdd";
                break;
            case month:
                pattern = "yyyyMM";
                break;
            case year:
                pattern = "yyyy";
                break;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String dateString = format.format(date);
        DebugLog.d(TAG, "dateString " + dateString);
        switch (type) {
            case OPERATE_TAB:
                return "operate_tab_" +dateString;
            default:
                break;
        }
        return "";
    }

    public static void saveTable(Table table) {
        if (table instanceof OperateDataTable) {
            OperateDataTable lastRecord = LitePal.findLast(OperateDataTable.class);
            float remain  = 0;
            if (lastRecord != null) {
                remain = lastRecord.getRemain();
            }
            if (((OperateDataTable)table).isIncome()) {
                remain += ((OperateDataTable)table).getSpend();
            } else {
                remain -= ((OperateDataTable)table).getSpend();
            }
            ((OperateDataTable)table).setRemain(remain);

            ((OperateDataTable)table).save();
        }
    }

    public static List query(TableType type, String... condition) {
        if (type == TableType.OPERATE_TAB) {
            List<OperateDataTable> list;
            list = LitePal.where(condition).find(OperateDataTable.class);
            DebugLog.d(TAG, "query operate " + list.size());
            return list;
        }
        return null;
    }

    /****************************** private methods ******************************/



}
