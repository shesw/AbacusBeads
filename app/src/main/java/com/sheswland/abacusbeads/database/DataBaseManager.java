package com.sheswland.abacusbeads.database;

import com.sheswland.abacusbeads.database.database_interface.Table;
import com.sheswland.abacusbeads.database.tables.AccountDayTable;
import com.sheswland.abacusbeads.database.tables.AccountMonthAndYearTable;
import com.sheswland.abacusbeads.database.tables.OperateDataTable;
import com.sheswland.abacusbeads.utils.DebugLog;
import com.sheswland.abacusbeads.utils.TextUtil;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        all_month,
        all_year,
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
        String prefix = "operate_tab_";
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
            case all_month:
                return prefix + "all_month";
            case all_year:
                return prefix + "all_year";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String dateString = format.format(date);
        DebugLog.d(TAG, "dateString " + dateString);
        switch (type) {
            case OPERATE_TAB:
            case ACCOUNT_DAY:
            case ACCOUNT_MONTH_AND_YEAR:
                return prefix + dateString;
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
            List<AccountMonthAndYearTable> list = LitePal.where(condition).find(AccountMonthAndYearTable.class);
            DebugLog.d(TAG, "query operate " + list.size());
            return list;
        }

        return null;
    }

    public int delete(TableType type, String... condition) {
        if (type == TableType.OPERATE_TAB) {
            return LitePal.deleteAll(OperateDataTable.class, condition);
        } else if (type == TableType.ACCOUNT_DAY) {
            return LitePal.deleteAll(AccountDayTable.class, condition);
        } else if (type == TableType.ACCOUNT_MONTH_AND_YEAR) {
            return LitePal.deleteAll(AccountMonthAndYearTable.class, condition);
        }
        return 1;
    }

    public Table deepCopyTable(Table table) {
        if (table instanceof OperateDataTable) {
            OperateDataTable res = new OperateDataTable();
            res.setTable_id(((OperateDataTable) table).getTable_id());
            res.setDate(((OperateDataTable) table).getDate());
            res.setRemain(((OperateDataTable) table).getRemain());
            res.setSecond(((OperateDataTable) table).getSecond());
            res.setContent(((OperateDataTable) table).getContent());
            res.setYear(((OperateDataTable) table).getYear());
            res.setMonth(((OperateDataTable) table).getMonth());
            res.setDay(((OperateDataTable) table).getDay());
            res.setHour(((OperateDataTable) table).getHour());
            res.setMinute(((OperateDataTable) table).getMinute());
            res.setSecond(((OperateDataTable) table).getSecond());
            res.setIncome(((OperateDataTable) table).isIncome());
            return res;
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
        String tableId = getTabeId(TableType.ACCOUNT_MONTH_AND_YEAR, table.getDate(), FilterAccuracy.all_month);
        DebugLog.d(TAG, "saveAccountMonthTable tableId " + tableId);
        String date = TextUtil.formatDate2yyyyMM(table);
        saveAccountMonthAndYearTable(table, tableId, date, true);
    }

    private void saveAccountYearTable(OperateDataTable table) {
        String tableId = getTabeId(TableType.ACCOUNT_MONTH_AND_YEAR, table.getDate(), FilterAccuracy.all_year);
        DebugLog.d(TAG, "saveAccountYearTable tableId " + tableId);
        String date = table.getYear() + "";
        saveAccountMonthAndYearTable(table, tableId, date, false);
    }

    private void saveAccountMonthAndYearTable(OperateDataTable table, String tableId, String date, boolean isMonth) {

        ArrayList<AccountMonthAndYearTable> result = null;

        boolean isAnotherMonth;

        if (isMonth) {
            result= (ArrayList<AccountMonthAndYearTable>) LitePal.where( "table_id = ? and date = ?", tableId, table.getYear() + "" + table.getMonth() + "").find(AccountMonthAndYearTable.class);
        } else {
            result= (ArrayList<AccountMonthAndYearTable>) LitePal.where("table_id = ? and date = ?", tableId, table.getYear() +"").find(AccountMonthAndYearTable.class);
        }
        AccountMonthAndYearTable table1 = null;
        if (result.size() > 0) {
            table1 = result.get(0);
        } else {
            table1 = new AccountMonthAndYearTable();
        }

        AccountMonthAndYearTable monthAndYearTable = new AccountMonthAndYearTable();
        monthAndYearTable.setTable_id(tableId);
        DebugLog.d(TAG, "saveAccountMonthAndYearTable tableId " + tableId);
        monthAndYearTable.setDate(date);
        if (table.isIncome()) {
            monthAndYearTable.setIncome(table1.getIncome() + table.getSpend());
            monthAndYearTable.setSpend(table1.getSpend());
            monthAndYearTable.setRemain(table1.getRemain() + table.getSpend());
        } else {
            monthAndYearTable.setIncome(table1.getIncome());
            monthAndYearTable.setSpend(table1.getSpend() + table.getSpend());
            monthAndYearTable.setRemain(table1.getRemain() - table.getSpend());
        }

        if (isMonth) {
            delete(TableType.ACCOUNT_MONTH_AND_YEAR, "table_id = ? and date = ?", tableId, table.getYear() + "" + table.getMonth() + "");
        } else {
            delete(TableType.ACCOUNT_MONTH_AND_YEAR, "table_id = ? and date = ?", tableId, table.getYear() +"");
        }
        monthAndYearTable.save();
    }
}