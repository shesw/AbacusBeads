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

import static com.sheswland.abacusbeads.utils.Const.TableType;
import static com.sheswland.abacusbeads.utils.Const.FilterAccuracy;
import static com.sheswland.abacusbeads.utils.Const.OPERATE_TABLE_PREFIX;

public class DataBaseManager {

    private final static String TAG = "DataBaseManager";

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
                String tableId = getTableId(type, date, FilterAccuracy.month);
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

    public String getTableId(TableType type, String date) {
        switch (type) {
            case OPERATE_TAB:
            case ACCOUNT_DAY:
            case ACCOUNT_MONTH_AND_YEAR:
                return OPERATE_TABLE_PREFIX + date;
            default:
                break;
        }
        return "";
    }

    public String getTableId(TableType type, Date date, FilterAccuracy accuracy) {
        String pattern = "yyyyMM";
        switch (accuracy) {
            case timestamp:
                return OPERATE_TABLE_PREFIX + System.currentTimeMillis();
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
                return OPERATE_TABLE_PREFIX + "all_month";
            case all_year:
                return OPERATE_TABLE_PREFIX + "all_year";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String dateString = format.format(date);
        DebugLog.d(TAG, "dateString " + dateString);
        switch (type) {
            case OPERATE_TAB:
            case ACCOUNT_DAY:
            case ACCOUNT_MONTH_AND_YEAR:
                return OPERATE_TABLE_PREFIX + dateString;
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

    public int deleteLastRecord() {

        OperateDataTable operateDataTable = LitePal.findLast(OperateDataTable.class);
        operateDataTable.delete();
        
        AccountDayTable dayTable = LitePal.findLast(AccountDayTable.class);
        DebugLog.d(TAG, "here " + (dayTable == null));
        if (dayTable == null) {
            return -1;
        }
        DebugLog.d(TAG, dayTable.getTable_id());
        LitePal.deleteAll(AccountDayTable.class, "table_id = ?", dayTable.getTable_id());

        AccountMonthAndYearTable monthTable = LitePal.where("table_id = ? and year = ? and month = ?",
                getTableId(TableType.ACCOUNT_MONTH_AND_YEAR, null, FilterAccuracy.all_month),
                dayTable.getYear()+ "",
                dayTable.getMonth() + "")
                .find(AccountMonthAndYearTable.class).get(0);
        AccountMonthAndYearTable yearTable = LitePal.where("table_id = ? and year = ?",
                getTableId(TableType.ACCOUNT_MONTH_AND_YEAR, null, FilterAccuracy.all_year),
                dayTable.getYear()+ "").
                find(AccountMonthAndYearTable.class).get(0);
        if (dayTable.isIncome()) {
            monthTable.setIncome(monthTable.getIncome() - dayTable.getSpend());
            monthTable.setRemain(monthTable.getRemain() - dayTable.getSpend());
            yearTable.setIncome(yearTable.getIncome() - dayTable.getSpend());
            yearTable.setRemain(yearTable.getRemain() - dayTable.getSpend());
        } else {
            monthTable.setSpend(monthTable.getSpend() - dayTable.getSpend());
            monthTable.setRemain(monthTable.getRemain() + dayTable.getSpend());
            yearTable.setSpend(yearTable.getSpend() - dayTable.getSpend());
            yearTable.setRemain(yearTable.getRemain() + dayTable.getSpend());
        }

        monthTable.save();
        yearTable.save();

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
        String tableId = getTableId(TableType.ACCOUNT_DAY, table.getDate(), FilterAccuracy.timestamp);
        AccountDayTable dayTable = new AccountDayTable();
        dayTable.setTable_id(tableId);
        dayTable.setContent(table.getContent());
        dayTable.setDate(TextUtil.formatDate2yyyyMMdd(table));
        dayTable.setSpend(table.getSpend());
        dayTable.setIncome(table.isIncome());
        dayTable.setRemain(table.getRemain());
        dayTable.setYear(table.getYear());
        dayTable.setMonth(table.getMonth());
        dayTable.setDay(table.getDay());
        DebugLog.d(TAG, "table_id " + tableId);
        dayTable.save();
    }

    private void saveAccountMonthTable(OperateDataTable table) {
        String tableId = getTableId(TableType.ACCOUNT_MONTH_AND_YEAR, table.getDate(), FilterAccuracy.all_month);
        DebugLog.d(TAG, "saveAccountMonthTable tableId " + tableId);
        String date = TextUtil.formatDate2yyyyMM(table);

        ArrayList<AccountMonthAndYearTable> list = (ArrayList<AccountMonthAndYearTable>) LitePal.where("table_id = ? and year = ? and month = ?", tableId, String.valueOf(table.getYear()), String.valueOf(table.getMonth())).find(AccountMonthAndYearTable.class);
        AccountMonthAndYearTable accountMonthAndYearTable;
        DebugLog.d(TAG, "saveAccountMonthTable list.size() " + list.size() + " " + tableId + " " + String.valueOf(table.getYear()));
        if (list.size() > 0) {
            accountMonthAndYearTable = list.get(0);
        } else {
            accountMonthAndYearTable = new AccountMonthAndYearTable();
        }

        accountMonthAndYearTable.setTable_id(tableId);
        accountMonthAndYearTable.setYear(table.getYear());
        accountMonthAndYearTable.setMonth(table.getMonth());
        accountMonthAndYearTable.setDay(table.getDay());
        accountMonthAndYearTable.setDate(date);
        if (table.isIncome()) {
            accountMonthAndYearTable.setIncome(accountMonthAndYearTable.getIncome() + table.getSpend());
            accountMonthAndYearTable.setSpend(accountMonthAndYearTable.getSpend());
            accountMonthAndYearTable.setRemain(accountMonthAndYearTable.getRemain() + table.getSpend());
        } else {
            accountMonthAndYearTable.setIncome(accountMonthAndYearTable.getIncome());
            accountMonthAndYearTable.setSpend(accountMonthAndYearTable.getSpend() + table.getSpend());
            accountMonthAndYearTable.setRemain(accountMonthAndYearTable.getRemain() - table.getSpend());
        }

//        delete(TableType.ACCOUNT_MONTH_AND_YEAR, "table_id = ? and year = ?", tableId, String.valueOf(table.getYear()));
        accountMonthAndYearTable.save();

    }

    private void saveAccountYearTable(OperateDataTable table) {
        String tableId = getTableId(TableType.ACCOUNT_MONTH_AND_YEAR, table.getDate(), FilterAccuracy.all_year);
        DebugLog.d(TAG, "saveAccountYearTable tableId " + tableId);
        String date = table.getYear() + "";

        ArrayList<AccountMonthAndYearTable> list = (ArrayList<AccountMonthAndYearTable>) LitePal.where("table_id = ? and year = ?", tableId, String.valueOf(table.getYear())).find(AccountMonthAndYearTable.class);
        AccountMonthAndYearTable accountMonthAndYearTable;
        if (list.size() > 0) {
            accountMonthAndYearTable = list.get(0);
        } else {
            accountMonthAndYearTable = new AccountMonthAndYearTable();
        }

        accountMonthAndYearTable.setTable_id(tableId);
        accountMonthAndYearTable.setYear(table.getYear());
        accountMonthAndYearTable.setMonth(table.getMonth());
        accountMonthAndYearTable.setDay(table.getDay());
        accountMonthAndYearTable.setDate(date);
        if (table.isIncome()) {
            accountMonthAndYearTable.setIncome(accountMonthAndYearTable.getIncome() + table.getSpend());
            accountMonthAndYearTable.setSpend(accountMonthAndYearTable.getSpend());
            accountMonthAndYearTable.setRemain(accountMonthAndYearTable.getRemain() + table.getSpend());
        } else {
            accountMonthAndYearTable.setIncome(accountMonthAndYearTable.getIncome());
            accountMonthAndYearTable.setSpend(accountMonthAndYearTable.getSpend() + table.getSpend());
            accountMonthAndYearTable.setRemain(accountMonthAndYearTable.getRemain() - table.getSpend());
        }

//        delete(TableType.ACCOUNT_MONTH_AND_YEAR, "table_id = ?", tableId);
        accountMonthAndYearTable.save();
    }

}