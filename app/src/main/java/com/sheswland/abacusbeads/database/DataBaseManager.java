package com.sheswland.abacusbeads.database;

import com.sheswland.abacusbeads.database.database_interface.Table;
import com.sheswland.abacusbeads.database.tables.AccountDayTable;
import com.sheswland.abacusbeads.database.tables.AccountMonthAndYearTable;
import com.sheswland.abacusbeads.database.tables.OperateDataTable;
import com.sheswland.abacusbeads.utils.DebugLog;
import com.sheswland.abacusbeads.utils.TextUtil;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataBaseManager {

    private final static String TAG = "DataBaseManager";

    public enum TableType {
        OPERATE_TAB,
        ACCOUNT_DAY,
        ACCOUNT_MONTH_AND_YEAR
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

    public Table produceTable(TableType type, Date date, Table originTable) {
        switch (type) {
            case OPERATE_TAB:
                String tableId = getTabeId(type, date, FilterAccuracy.month);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                if (!(originTable instanceof OperateDataTable)) {
                    originTable = new OperateDataTable();
                }
                ((OperateDataTable) originTable).setTable_id(tableId);
                ((OperateDataTable) originTable).setYear(calendar.get(Calendar.YEAR));
                ((OperateDataTable) originTable).setMonth(calendar.get(Calendar.MONTH) + 1);
                ((OperateDataTable) originTable).setDay(calendar.get(Calendar.DAY_OF_MONTH));
                ((OperateDataTable) originTable).setHour(calendar.get(Calendar.HOUR));
                ((OperateDataTable) originTable).setMinute(calendar.get(Calendar.MINUTE));
                ((OperateDataTable) originTable).setSecond(calendar.get(Calendar.SECOND));
                ((OperateDataTable) originTable).setDate(date);
                return originTable;
            default:
                break;

        }
        return null;
    }

    public String getTabeId(TableType type, Date date, FilterAccuracy accuracy) {
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
            case ACCOUNT_DAY:
            case ACCOUNT_MONTH_AND_YEAR:
                return "operate_tab_" + dateString;
            default:
                break;
        }
        return "";
    }

    public void saveTable(Table table) {
        if (table instanceof OperateDataTable) {
            OperateDataTable table1 = saveOperateTable(table);
            saveAccountDayTable(table1);
            saveAccountMonthTable(table1);
            saveAccountYearTable(table1);
        }
    }

    public List query(TableType type, String... condition) {
        if (type == TableType.OPERATE_TAB) {
            List<OperateDataTable> list;
            list = LitePal.where(condition).find(OperateDataTable.class);
            DebugLog.d(TAG, "query operate " + list.size());
            return list;
        } else if (type == TableType.ACCOUNT_DAY) {
            List<AccountDayTable> list;
            list = LitePal.where(condition).find(AccountDayTable.class);
            DebugLog.d(TAG, "query operate " + list.size());
            return list;
        } else if (type == TableType.ACCOUNT_MONTH_AND_YEAR) {
            List<AccountMonthAndYearTable> list;
            list = LitePal.where(condition).find(AccountMonthAndYearTable.class);
            DebugLog.d(TAG, "query operate " + list.size());
            return list;
        }

        return null;
    }

    /****************************** private methods ******************************/
    private OperateDataTable saveOperateTable(Table table) {
        OperateDataTable lastRecord = LitePal.findLast(OperateDataTable.class);
        float remain = 0;
        if (lastRecord != null) {
            remain = lastRecord.getRemain();
        }
        if (((OperateDataTable) table).isIncome()) {
            remain += ((OperateDataTable) table).getSpend();
        } else {
            remain -= ((OperateDataTable) table).getSpend();
        }
        ((OperateDataTable) table).setRemain(remain);
        ((OperateDataTable) table).save();
        return ((OperateDataTable) table);
    }

    private void saveAccountDayTable(OperateDataTable table) {
        String tableId = getTabeId(TableType.ACCOUNT_DAY, table.getDate(), FilterAccuracy.month);
        AccountDayTable dayTable = new AccountDayTable();
        dayTable.setTable_id(tableId);
        dayTable.setContent(table.getContent());
        dayTable.setDate(TextUtil.formatDate2yyyyMMdd(table));
        dayTable.setSpend(table.getSpend());
        dayTable.setIncome(table.isIncome());
        dayTable.setRemain(table.getRemain());
        DebugLog.d(TAG, "table_id " + tableId);
        dayTable.save();
    }

    private void saveAccountMonthTable(OperateDataTable table) {
        String tableId = getTabeId(TableType.ACCOUNT_MONTH_AND_YEAR, table.getDate(), FilterAccuracy.month);
        String date = TextUtil.formatDate2yyyyMM(table);
        saveAccountMonthAndYearTable(table, tableId, date);
    }

    private void saveAccountYearTable(OperateDataTable table) {
        String tableId = getTabeId(TableType.ACCOUNT_MONTH_AND_YEAR, table.getDate(), FilterAccuracy.year);
        String date = table.getYear() + "";
        saveAccountMonthAndYearTable(table, tableId, date);
    }

    private void saveAccountMonthAndYearTable(OperateDataTable table, String tableId, String date) {
        AccountMonthAndYearTable monthAndYearTable = null;
        try {
            monthAndYearTable = (AccountMonthAndYearTable) LitePal.where("table_id = ?", tableId).find(AccountMonthAndYearTable.class);
        } catch (Exception e) {
            monthAndYearTable = new AccountMonthAndYearTable();
        }
        monthAndYearTable.setTable_id(tableId);
        monthAndYearTable.setDate(date);
        if (table.isIncome()) {
            monthAndYearTable.setIncome(monthAndYearTable.getIncome() + table.getSecond());
            monthAndYearTable.setRemain(monthAndYearTable.getRemain() + table.getSecond());
        } else {
            monthAndYearTable.setSpend(monthAndYearTable.getSpend() + table.getSecond());
            monthAndYearTable.setRemain(monthAndYearTable.getRemain() - table.getSecond());
        }
        monthAndYearTable.save();
    }
}