package com.sheswland.abacusbeads.query;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sheswland.abacusbeads.BaseActivity;
import com.sheswland.abacusbeads.R;
import com.sheswland.abacusbeads.database.DataBaseManager;
import com.sheswland.abacusbeads.database.tables.OperateDataTable;
import com.sheswland.abacusbeads.query.adapter.QueryAdapter;
import com.sheswland.abacusbeads.utils.DebugLog;

import java.util.ArrayList;
import java.util.Date;

public class QueryActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "QueryActivity";

    private final String[] types = new String[] {"类型", "支出", "收入"};

    private Activity mActivity;
    private RecyclerView queryList;
    private QueryAdapter queryAdapter;
    private TextView btPrint;
    private TextView btOpenFileSystem;
    private TextView btDate;
    private TextView btType;

    private int currentType;

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
        btPrint = findViewById(R.id.bt_print);
        btOpenFileSystem = findViewById(R.id.bt_open_file_system);
        queryList = findViewById(R.id.query_list);
        btDate = findViewById(R.id.bt_date);
        btType = findViewById(R.id.bt_type);
    }

    private void initViews() {
        btPrint.setOnClickListener(this);
        btOpenFileSystem.setOnClickListener(this);
        btDate.setOnClickListener(this);
        btType.setOnClickListener(this);
        queryAdapter = new QueryAdapter(mActivity);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mActivity);
        queryList.setLayoutManager(manager);
        queryList.setAdapter(queryAdapter);
    }

    private void initQueryData() {
        Date date = new Date();
        String[] queryCondition = new String[] {"tableId = ?", DataBaseManager.getTabeId(DataBaseManager.TableType.OPERATE_TAB, date, DataBaseManager.FilterAccuracy.month)};
        ArrayList<OperateDataTable> list = (ArrayList<OperateDataTable>) DataBaseManager.query(DataBaseManager.TableType.OPERATE_TAB, queryCondition[0], queryCondition[1]);
        queryAdapter.setData(list);
        queryAdapter.notifyDataSetChanged();
    }

    private void changeType() {
        currentType++;
        if (currentType >= types.length) {
            currentType = 0;
        }
        btType.setText(types[currentType]);

        Date date = new Date();
        if (currentType == 0) {
            String[] queryCondition = new String[]{"tableId = ?", DataBaseManager.getTabeId(DataBaseManager.TableType.OPERATE_TAB, date, DataBaseManager.FilterAccuracy.month)};
            ArrayList<OperateDataTable> list = (ArrayList<OperateDataTable>) DataBaseManager.query(DataBaseManager.TableType.OPERATE_TAB, queryCondition[0], queryCondition[1]);
            queryAdapter.setData(list);
            queryAdapter.notifyDataSetChanged();
        } else if (currentType == 1) {
            String[] queryCondition = new String[]{"tableId = ? and isIncome = ?", DataBaseManager.getTabeId(DataBaseManager.TableType.OPERATE_TAB, date, DataBaseManager.FilterAccuracy.month), "0"};
            ArrayList<OperateDataTable> list = (ArrayList<OperateDataTable>) DataBaseManager.query(DataBaseManager.TableType.OPERATE_TAB, queryCondition[0], queryCondition[1], queryCondition[2]);
            queryAdapter.setData(list);
            queryAdapter.notifyDataSetChanged();
        } else if (currentType == 2) {
            String[] queryCondition = new String[]{"tableId = ? and isIncome = ?", DataBaseManager.getTabeId(DataBaseManager.TableType.OPERATE_TAB, date, DataBaseManager.FilterAccuracy.month), "1"};
            ArrayList<OperateDataTable> list = (ArrayList<OperateDataTable>) DataBaseManager.query(DataBaseManager.TableType.OPERATE_TAB, queryCondition[0], queryCondition[1], queryCondition[2]);
            queryAdapter.setData(list);
            queryAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_print) {
            DebugLog.d(TAG, "bt print");
        } else if (id == R.id.bt_open_file_system) {
            DebugLog.d(TAG, "bt open file system");
        } else if (id == R.id.bt_date) {

        } else if (id == R.id.bt_type) {
            changeType();
        }
    }
}
