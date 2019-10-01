package com.sheswland.abacusbeads.query;

import com.sheswland.abacusbeads.database.DataBaseManager;
import com.sheswland.abacusbeads.database.database_interface.Table;
import com.sheswland.abacusbeads.database.tables.AccountDayTable;
import com.sheswland.abacusbeads.database.tables.AccountMonthAndYearTable;
import com.sheswland.abacusbeads.utils.Const;

import java.util.ArrayList;

import static com.sheswland.abacusbeads.utils.Const.TableType.ACCOUNT_MONTH_AND_YEAR;

public class QueryDataManager {
    private final static String TAG = "QueryDataManager";
    private static QueryDataManager _HOLDER;
    public static QueryDataManager getInstance(){
        if (_HOLDER == null) {
            _HOLDER = new QueryDataManager();
        }
        return _HOLDER;
    }
    /****************** private field ************************/
    private ArrayList<Table> mDayTableList;
    private ArrayList<Table> mMonthTableList;
    private ArrayList<Table> mYearTableList;

    // need show in password mode
    private boolean psDaySpend = true;
    private boolean psDayRemain = true;
    private boolean psMYSpend = true;
    private boolean psMYIncome = true;
    private boolean psMYRemain = true;


    /*********************** private methods *********************/
    private QueryDataManager() {
        init();
    }
    private void init() {
        mDayTableList = new ArrayList<>();
        mMonthTableList = new ArrayList<>();
        mYearTableList = new ArrayList<>();
    }

    private void updateDayTableList(String... condition) {
        ArrayList<AccountDayTable> list = (ArrayList<AccountDayTable>) DataBaseManager.getInstance().query(Const.TableType.ACCOUNT_DAY, condition);
        this.mDayTableList.clear();
        this.mDayTableList.addAll(list);
    }

    /******************** public methods **************************/

    public void updateDayTableList(int year, int month) {
        updateDayTableList("year = ? and month = ?", String.valueOf(year), String.valueOf(month));
    }

    public void updateDayTableList(int year, int month, boolean isIncome) {
        updateDayTableList("year = ? and month = ? and isIncome = ?", String.valueOf(year), String.valueOf(month), isIncome ? "1" : "0");
    }

    public void updateMontTableList(int year) {
        ArrayList<AccountMonthAndYearTable> list = (ArrayList<AccountMonthAndYearTable>) DataBaseManager.getInstance().query(ACCOUNT_MONTH_AND_YEAR,
                "table_id = ? and year = ?",
                DataBaseManager.getInstance().getTableId(ACCOUNT_MONTH_AND_YEAR, null, Const.FilterAccuracy.all_month),
                String.valueOf(year));
        this.mMonthTableList.clear();
        this.mMonthTableList.addAll(list);
    }

    public void updateYearTableList() {
        ArrayList<AccountMonthAndYearTable> list = (ArrayList<AccountMonthAndYearTable>) DataBaseManager.getInstance().query(ACCOUNT_MONTH_AND_YEAR,
                "table_id = ?",
                DataBaseManager.getInstance().getTableId(ACCOUNT_MONTH_AND_YEAR, null, Const.FilterAccuracy.all_year));
        this.mYearTableList.clear();
        this.mYearTableList.addAll(list);
    }

    public ArrayList<Table> getmDayTableList() {
        return mDayTableList;
    }

    public void setmDayTableList(ArrayList<Table> mDayTableList) {
        this.mDayTableList = mDayTableList;
    }

    public ArrayList<Table> getmMonthTableList() {
        return mMonthTableList;
    }

    public void setmMonthTableList(ArrayList<Table> mMonthTableList) {
        this.mMonthTableList = mMonthTableList;
    }

    public ArrayList<Table> getmYearTableList() {
        return mYearTableList;
    }

    public void setmYearTableList(ArrayList<Table> mYearTableList) {
        this.mYearTableList = mYearTableList;
    }

    public void changePsDaySpend() {
        this.psDaySpend = !psDaySpend;
    }

    public void changePsDayRemain() {
        this.psDayRemain = !psDayRemain;
    }

    public void changePsMYSpend() {
        this.psMYSpend = !psMYSpend;
    }

    public void changePsMYIncome() {
        this.psMYIncome = !psMYIncome;
    }

    public void changePsMYRemain() {
        this.psMYRemain = !psMYRemain;
    }

    public boolean isPsDaySpend() {
        return psDaySpend;
    }

    public boolean isPsDayRemain() {
        return psDayRemain;
    }

    public boolean isPsMYSpend() {
        return psMYSpend;
    }

    public boolean isPsMYIncome() {
        return psMYIncome;
    }

    public boolean isPsMYRemain() {
        return psMYRemain;
    }
}
