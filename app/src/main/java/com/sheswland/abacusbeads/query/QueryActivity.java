package com.sheswland.abacusbeads.query;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sheswland.abacusbeads.BaseActivity;
import com.sheswland.abacusbeads.R;
import com.sheswland.abacusbeads.database.DataBaseManager;
import com.sheswland.abacusbeads.database.database_interface.Table;
import com.sheswland.abacusbeads.query.adapter.QueryAdapter;
import com.sheswland.abacusbeads.utils.DebugLog;

import java.util.ArrayList;
import java.util.Date;

public class QueryActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "QueryActivity";

    public static final String[] types = new String[] {"类型", "支出", "收入"};
    public enum accuracy{
        year,
        month,
        day
    }

    private Activity mActivity;
    private SimpleDraweeView logo;
    private RecyclerView queryList;
    private QueryAdapter queryAdapter;
    private TextView btPrint;
    private TextView btOpenFileSystem;

    public int currentType;
    private int currentAccuracy;
    private String currentTableId;
    private DataBaseManager.TableType currentTableType;
    private Date currentDate;
    private QueryAdapter.TitleBarListener adapterTitleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        mActivity = this;
        findViews();
        initViews();
        initQueryData();
    }

    private void findViews() {
        logo = findViewById(R.id.logo);
        btPrint = findViewById(R.id.bt_print);
        btOpenFileSystem = findViewById(R.id.bt_open_file_system);
        queryList = findViewById(R.id.query_list);
    }

    private void initViews() {
        logo.setOnClickListener(this);
        btPrint.setOnClickListener(this);
        btOpenFileSystem.setOnClickListener(this);

        adapterTitleListener = new QueryAdapter.TitleBarListener() {
            @Override
            public void onDateClick() {

            }

            @Override
            public void onTypeClick() {
                changeType();
            }
        };
        queryAdapter = new QueryAdapter(mActivity, adapterTitleListener);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mActivity);
        queryList.setLayoutManager(manager);
        queryList.setAdapter(queryAdapter);
    }

    private void initQueryData() {
        currentTableType = DataBaseManager.TableType.ACCOUNT_DAY;
        currentDate = new Date();
        currentTableId = DataBaseManager.getInstance().getTabeId(currentTableType, currentDate, DataBaseManager.FilterAccuracy.month);
        DebugLog.d(TAG, "currentTableId " + currentTableId);
        String[] queryCondition = new String[] {"table_id = ?", currentTableId};
        ArrayList<Table> list = (ArrayList<Table>) DataBaseManager.getInstance().query(currentTableType, queryCondition[0], queryCondition[1]);
        queryAdapter.setData(list);
        queryAdapter.notifyDataSetChanged();

    }

    private void changeType() {
        currentType++;
        if (currentType >= types.length) {
            currentType = 0;
        } else if (currentType < 0) {
            currentType = types.length - 1;
        }
        if (currentType == 0) {
            String[] queryCondition = new String[]{"table_id = ?", currentTableId};
            ArrayList<Table> list = (ArrayList<Table>) DataBaseManager.getInstance().query(currentTableType, queryCondition[0], queryCondition[1]);
            queryAdapter.setData(list);
            queryAdapter.notifyDataSetChanged();
        } else if (currentType == 1) {
            String[] queryCondition = new String[]{"table_id = ? and isIncome = ?",currentTableId, "0"};
            ArrayList<Table> list = (ArrayList<Table>) DataBaseManager.getInstance().query(currentTableType, queryCondition[0], queryCondition[1], queryCondition[2]);
            queryAdapter.setData(list);
            queryAdapter.notifyDataSetChanged();
        } else if (currentType == 2) {
            String[] queryCondition = new String[]{"table_id = ? and isIncome = ?", currentTableId, "1"};
            ArrayList<Table> list = (ArrayList<Table>) DataBaseManager.getInstance().query(currentTableType, queryCondition[0], queryCondition[1], queryCondition[2]);
            queryAdapter.setData(list);
            queryAdapter.notifyDataSetChanged();
        }
    }

    private void changeAccuracy() {
        currentAccuracy++;
        if (currentAccuracy >= accuracy.values().length) {
            currentAccuracy = 0;
        } else if (currentAccuracy < 0) {
            currentAccuracy = accuracy.values().length - 1;
        }

        if (currentAccuracy == 0) {
            queryAdapter = new QueryAdapter(mActivity, adapterTitleListener);
            queryList.setAdapter(queryAdapter);
            currentTableType = DataBaseManager.TableType.ACCOUNT_DAY;
            queryAdapter.setAccuracy(accuracy.day);
            currentType--;
            changeType();
        } else if (currentAccuracy == 1) {
            queryAdapter = new QueryAdapter(mActivity, adapterTitleListener);
            queryList.setAdapter(queryAdapter);
            queryAdapter.setAccuracy(accuracy.month);
            currentTableType = DataBaseManager.TableType.ACCOUNT_MONTH_AND_YEAR;
            String[] condition = new String[] {"table_id = ?", DataBaseManager.getInstance().getTabeId(currentTableType, currentDate, DataBaseManager.FilterAccuracy.year)};
            ArrayList<Table> list = (ArrayList<Table>) DataBaseManager.getInstance().query(currentTableType, condition[0], condition[1]);
            queryAdapter.setData(list);
            queryAdapter.notifyDataSetChanged();
        } else if (currentAccuracy == 2) {
            queryAdapter = new QueryAdapter(mActivity, adapterTitleListener);
            queryList.setAdapter(queryAdapter);
            queryAdapter.setAccuracy(accuracy.year);
            currentTableType = DataBaseManager.TableType.ACCOUNT_MONTH_AND_YEAR;
            String[] condition = new String[] {"table_id = ?", DataBaseManager.getInstance().getTabeId(currentTableType, currentDate, DataBaseManager.FilterAccuracy.all)};
            ArrayList<Table> list = (ArrayList<Table>) DataBaseManager.getInstance().query(currentTableType, condition[0], condition[1]);
            queryAdapter.setData(list);
            queryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.logo) {
            changeAccuracy();
        } else if (id == R.id.bt_print) {
            DebugLog.d(TAG, "bt print");
        } else if (id == R.id.bt_open_file_system) {
            DebugLog.d(TAG, "bt open file system");
        }
    }
}
