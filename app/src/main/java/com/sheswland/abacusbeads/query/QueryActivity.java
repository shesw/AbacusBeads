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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QueryActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "QueryActivity";

    private Activity mActivity;
    private RecyclerView queryList;
    private QueryAdapter queryAdapter;
    private TextView btPrint;
    private TextView btOpenFileSystem;

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
    }

    private void initViews() {
        btPrint.setOnClickListener(this);
        btOpenFileSystem.setOnClickListener(this);
        queryAdapter = new QueryAdapter(mActivity);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mActivity);
        queryList.setLayoutManager(manager);
        queryList.setAdapter(queryAdapter);
    }

    private void initQueryData() {
        Date date = new Date();
        ArrayList<OperateDataTable> list = (ArrayList<OperateDataTable>) DataBaseManager.query(DataBaseManager.TableType.OPERATE_TAB, DataBaseManager.getTabeId(DataBaseManager.TableType.OPERATE_TAB, date, DataBaseManager.FilterAccuracy.month));
        for (OperateDataTable table : list) {
            DebugLog.d(TAG, "year " + table.getYear());
        }
        queryAdapter.setData(list);
        queryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_print) {
            DebugLog.d(TAG, "bt print");
        } else if (id == R.id.bt_open_file_system) {
            DebugLog.d(TAG, "bt open file system");
        }
    }
}
