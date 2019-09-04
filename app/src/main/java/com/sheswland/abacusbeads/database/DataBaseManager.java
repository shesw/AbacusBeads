package com.sheswland.abacusbeads.database;

import com.sheswland.abacusbeads.database.database_interface.Table;
import com.sheswland.abacusbeads.database.tables.OperateDataTable;
import com.sheswland.abacusbeads.utils.DebugLog;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
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
                if (originTable instanceof OperateDataTable) {
                    ((OperateDataTable)originTable).setTableId(tableId);
                    return originTable;
                } else {
                    OperateDataTable operateData = new OperateDataTable();
                    operateData.setTableId(tableId);
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
        StringBuilder queryCondition = new StringBuilder();
        for (String c : condition) {
         queryCondition.append(c).append(";");
        }
        queryCondition.delete(queryCondition.length() - 1, queryCondition.length());
        if (type == TableType.OPERATE_TAB) {
            List<OperateDataTable> list;
            list = (List<OperateDataTable>) LitePal.findAll(OperateDataTable.class);
            DebugLog.d(TAG, "query operate " + list.size());
            return list;
        }
        return null;
    }

    /****************************** private methods ******************************/



}
