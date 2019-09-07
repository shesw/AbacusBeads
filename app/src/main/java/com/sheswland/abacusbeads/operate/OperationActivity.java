package com.sheswland.abacusbeads.operate;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.sheswland.abacusbeads.BaseActivity;
import com.sheswland.abacusbeads.R;
import com.sheswland.abacusbeads.database.DataBaseManager;
import com.sheswland.abacusbeads.database.tables.AccountDayTable;
import com.sheswland.abacusbeads.database.tables.AccountMonthAndYearTable;
import com.sheswland.abacusbeads.database.tables.OperateDataTable;
import com.sheswland.abacusbeads.utils.DebugLog;
import com.sheswland.abacusbeads.utils.JumperHelper;
import com.sheswland.abacusbeads.utils.TextUtil;
import com.sheswland.abacusbeads.utils.TipUtils;

import org.litepal.LitePal;
import java.util.Date;

import static com.sheswland.abacusbeads.utils.TextUtil.getTime;

public class OperationActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private final String TAG = "OperationActivity";

    private Activity mActivity;

    private TextView inputDate;
    private EditText inputContent;
    private EditText inputSpend;
    private RadioGroup radioGroup;
    private TextView btCommit;
    private TextView btQuery;
    private TextView btReset;
    private OperateDataTable operateDataTable;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);

        mActivity = this;
        mHandler = new Handler(getMainLooper());

        findViews();
        initViews();
        initDataTable(null);
        initTimer();
    }

    private void findViews() {
        inputDate = findViewById(R.id.input_date);
        inputContent = findViewById(R.id.input_content);
        inputSpend = findViewById(R.id.input_spend);
        radioGroup = findViewById(R.id.radio_group);
        btCommit = findViewById(R.id.bt_commit);
        btQuery = findViewById(R.id.bt_query);
        btReset = findViewById(R.id.bt_reset);
    }

    private void initViews() {
        inputDate.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        btCommit.setOnClickListener(this);
        btQuery.setOnClickListener(this);
        btReset.setOnClickListener(this);
    }

    private void initDataTable(Date date) {
        if (date == null) {
            date = new Date();
        }
        operateDataTable = new OperateDataTable();
        inputDate.setText(getTime(date));
        operateDataTable = (OperateDataTable) DataBaseManager.getInstance().produceTable(DataBaseManager.TableType.OPERATE_TAB, date, operateDataTable);
    }

    private void initTimer() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Date date1 = new Date();
                inputDate.setText(getTime(date1));
                initTimer();
            }
        }, 700);
    }

    /************** implement interface methods ***************/
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.input_date) {
            DebugLog.d(TAG, "input date");
            showDatePicker();
        } else if (id == R.id.bt_commit) {
           commit();
        } else if (id == R.id.bt_query) {
            DebugLog.d(TAG, "bt_query");
            JumperHelper.jump2Query(mActivity);
        } else if (id == R.id.bt_reset) {
            DebugLog.d(TAG, "bt_reset");
            TipUtils.showMidToast(mActivity, "还没想好这个按钮用来干嘛");
            LitePal.deleteAll(OperateDataTable.class);
            LitePal.deleteAll(AccountDayTable.class);
            LitePal.deleteAll(AccountMonthAndYearTable.class);
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_spend:
                DebugLog.d(TAG, "rb_spend");
                operateDataTable.setIncome(false);
                DebugLog.d(TAG, "operation data " + operateDataTable.getYear() + " " + operateDataTable.getMonth() + " " + operateDataTable.getDay());
                break;
            case R.id.rb_income:
                DebugLog.d(TAG, "rb_income");
                operateDataTable.setIncome(true);
                DebugLog.d(TAG, "operation data " + operateDataTable.getYear() + " " + operateDataTable.getMonth() + " " + operateDataTable.getDay());
                break;
        }
    }

    /**************************** private methods ****************/
    private void showDatePicker() {
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String dateString = getTime(date);
                DebugLog.d(TAG, "date" + dateString);
                inputDate.setText(dateString);
                operateDataTable = (OperateDataTable) DataBaseManager.getInstance().produceTable(DataBaseManager.TableType.OPERATE_TAB, date, operateDataTable);
                DebugLog.d(TAG, "date");
                DebugLog.d(TAG, operateDataTable.getYear() + "");
                DebugLog.d(TAG, operateDataTable.getMonth() + "");
                DebugLog.d(TAG, operateDataTable.getDay() + "");
                DebugLog.d(TAG, operateDataTable.getHour() + "");
                DebugLog.d(TAG, operateDataTable.getMinute() + "");
                DebugLog.d(TAG, operateDataTable.getSecond() + "");


            }
        }) .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                .setCancelText("Cancel")//取消按钮文字
                .setSubmitText("Sure")//确认按钮文字
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("Title")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTextColorCenter(Color.WHITE)
//                .setTextColorOut(0xffffff)
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
//                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
//                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();
        pvTime.show();
    }

    private void commit() {
        String content = inputContent.getText().toString();
        String spend = inputSpend.getText().toString();
        DebugLog.d(TAG, "bt_commit " + content + " " + spend + " ");
        if (TextUtil.isEmpty(content)) {
            TipUtils.showMidToast(mActivity, "请输入内容");
        } else if (TextUtil.isEmpty(spend)) {
            TipUtils.showMidToast(mActivity, "请输入金额");
        } else {
            operateDataTable.setContent(content);
            operateDataTable.setSpend(Float.parseFloat(spend));
            DataBaseManager.getInstance().saveTable(operateDataTable);
            initDataTable(operateDataTable.getDate());
            TipUtils.showMidToast(mActivity, "commit success");
        }
    }

}
