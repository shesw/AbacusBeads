package com.sheswland.abacusbeads.query;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sheswland.abacusbeads.BaseActivity;
import com.sheswland.abacusbeads.R;
import com.sheswland.abacusbeads.database.DataBaseManager;
import com.sheswland.abacusbeads.database.database_interface.Table;
import com.sheswland.abacusbeads.database.tables.OperateDataTable;
import com.sheswland.abacusbeads.query.adapter.QueryAdapter;
import com.sheswland.abacusbeads.utils.DebugLog;
import com.sheswland.abacusbeads.utils.TextUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class QueryActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "QueryActivity";

    public static final String[] types = new String[] {"类型", "支出", "收入"};

    private Activity mActivity;
    private SimpleDraweeView logo;
    private RecyclerView queryList;
    private QueryAdapter queryAdapter;
    private TextView btPrint;
    private TextView btOpenFileSystem;

    public int currentType;
    private Date currentDate;
    private int mYear;
    private int mMonth;
    private int mDay;
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
                chooseDate();
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
        Date date = new Date();
        int[] ymd = TextUtil.getYMD(date);
        currentDate = date;
        mYear = ymd[0];
        mMonth = ymd[1];
        mDay = ymd[2];
        QueryDataManager.getInstance().updateDayTableList(mYear, mMonth);
        queryAdapter.notifyDataSetChanged();
    }

    /******************* logic change *********************/
    private void changeAccuracy() {
        queryAdapter.currentAccuracy++;
        if (queryAdapter.currentAccuracy >= QueryAdapter.Accuracy.values().length) {
            queryAdapter.currentAccuracy = 0;
        } else if (queryAdapter.currentAccuracy < 0) {
            queryAdapter.currentAccuracy = QueryAdapter.Accuracy.values().length - 1;
        }

        if (queryAdapter.currentAccuracy == QueryAdapter.Accuracy.day.ordinal()) {
            if (currentType == 0) {
                QueryDataManager.getInstance().updateDayTableList(mYear, mMonth);
            } else {
                QueryDataManager.getInstance().updateDayTableList(mYear, mMonth, currentType != 1);
            }
            queryAdapter = new QueryAdapter(mActivity, adapterTitleListener);
            queryList.setAdapter(queryAdapter);
            queryAdapter.setAccuracy(QueryAdapter.Accuracy.day.ordinal());
            currentType--;
            changeType();
        } else if (queryAdapter.currentAccuracy == QueryAdapter.Accuracy.month.ordinal()) {
            QueryDataManager.getInstance().updateMontTableList(mYear);
            queryAdapter = new QueryAdapter(mActivity, adapterTitleListener);
            queryList.setAdapter(queryAdapter);
            queryAdapter.setAccuracy(QueryAdapter.Accuracy.month.ordinal());
            queryAdapter.notifyDataSetChanged();
        } else if (queryAdapter.currentAccuracy == QueryAdapter.Accuracy.year.ordinal()) {
            QueryDataManager.getInstance().updateYearTableList();
            queryAdapter = new QueryAdapter(mActivity, adapterTitleListener);
            queryList.setAdapter(queryAdapter);
            queryAdapter.setAccuracy(QueryAdapter.Accuracy.year.ordinal());
            queryAdapter.notifyDataSetChanged();
        }
    }

    private void changeType() {
        currentType++;
        if (currentType >= types.length) {
            currentType = 0;
        } else if (currentType < 0) {
            currentType = types.length - 1;
        }
        if (currentType == 0) {
            QueryDataManager.getInstance().updateDayTableList(mYear, mMonth);
            queryAdapter.notifyDataSetChanged();
        } else if (currentType == 1) {
            QueryDataManager.getInstance().updateDayTableList(mYear, mMonth, false);
            queryAdapter.notifyDataSetChanged();;
        } else if (currentType == 2) {
            QueryDataManager.getInstance().updateDayTableList(mYear, mMonth, true);
            queryAdapter.notifyDataSetChanged();
        }
    }

    private void chooseDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                currentType--;
                if (currentType >= types.length) {
                    currentType = 0;
                } else if (currentType < 0) {
                    currentType = types.length - 1;
                }
                int[] time = TextUtil.getYMD(date);
                currentDate = date;
                mYear = time[0];
                mMonth = time[1];
                mDay = time[2];
                changeType();
            }
        }) .setType(new boolean[]{true, true, false, false, false,false})// 默认全部显示
                .setCancelText("Cancel")//取消按钮文字
                .setSubmitText("Sure")//确认按钮文字
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("Title")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTextColorCenter(Color.WHITE)
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
                .setDate(calendar)// 如果不设置的话，默认是系统时间*/
//                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();
        pvTime.show();


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
