package com.sheswland.abacusbeads.query;

import android.Manifest;
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
import com.sheswland.abacusbeads.FileController;
import com.sheswland.abacusbeads.R;
import com.sheswland.abacusbeads.database.DataBaseManager;
import com.sheswland.abacusbeads.query.adapter.QueryAdapter;
import com.sheswland.abacusbeads.utils.Const;
import com.sheswland.abacusbeads.utils.DebugLog;
import com.sheswland.abacusbeads.utils.TextUtil;
import com.sheswland.abacusbeads.utils.TimeUtil;
import com.sheswland.abacusbeads.utils.TipUtils;
import com.sheswland.abacusbeads.utils.permission.PermissionHelper;
import com.sheswland.abacusbeads.utils.permission.PermissionInterface;
import com.sheswland.abacusbeads.utils.permission.PermissionUtil;

import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.sheswland.abacusbeads.utils.Const.dayTableIncomeType;

public class QueryActivity extends BaseActivity implements View.OnClickListener, PermissionInterface {

    private final String TAG = "QueryActivity";

    private Activity mActivity;
    private SimpleDraweeView logo;
    private RecyclerView queryList;
    private QueryAdapter queryAdapter;
    private TextView btPrint;
    private TextView btDeleteLastRecord;

    public int currentType;
    private Date currentDate;
    private int mYear;
    private int mMonth;
    private int mDay;

    private View includeDayTitle;
    private View includeMYTitle;
    private TextView tv_date;
    private TextView tv_type;

    private PermissionHelper permissionHelper;
//    private String[] mPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
    private String[] mPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        mActivity = this;
        findViews();
        initViews();
        initQueryData();
        initPermissionHelper();
    }

    private void findViews() {
        logo = findViewById(R.id.logo);
        btPrint = findViewById(R.id.bt_print);
        btDeleteLastRecord = findViewById(R.id.delete_last_record);
        queryList = findViewById(R.id.query_list);

        tv_date = findViewById(R.id.bt_date);
        tv_type = findViewById(R.id.bt_type);
        includeDayTitle = findViewById(R.id.include_query_title_day);
        includeMYTitle = findViewById(R.id.include_query_title_month_and_year);
    }

    private void initViews() {
        logo.setOnClickListener(this);
        btPrint.setOnClickListener(this);
        btDeleteLastRecord.setOnClickListener(this);

        tv_date.setOnClickListener(this);
        tv_type.setOnClickListener(this);

        queryAdapter = new QueryAdapter(mActivity);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mActivity);
        queryList.setLayoutManager(manager);
        queryList.setAdapter(queryAdapter);
    }

    private void initQueryData() {
        Date date = new Date();
        int[] ymd = TimeUtil.getYMD(date);
        currentDate = date;
        mYear = ymd[0];
        mMonth = ymd[1];
        mDay = ymd[2];
        QueryDataManager.getInstance().updateDayTableList(mYear, mMonth);
        queryAdapter.notifyDataSetChanged();
    }

    private void initPermissionHelper() {
        permissionHelper = new PermissionHelper(this, this);
    }

    private void handleTimeChoose(Date date) {
        if (queryAdapter.currentAccuracy == Const.Accuracy.day.ordinal()) {
            currentType--;
            if (currentType >= dayTableIncomeType.length) {
                currentType = 0;
            } else if (currentType < 0) {
                currentType = dayTableIncomeType.length - 1;
            }
            int[] time = TimeUtil.getYMD(date);
            currentDate = date;
            mYear = time[0];
            mMonth = time[1];
            mDay = time[2];
            changeType();
        } else if (queryAdapter.currentAccuracy == Const.Accuracy.month.ordinal()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            mYear = calendar.get(Calendar.YEAR) + 1;
            QueryDataManager.getInstance().updateMontTableList(mYear);
            queryAdapter.notifyDataSetChanged();
        }
    }

    private void popDeleteDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Delete Last Record")
                .setContentText("Sure to Delete")
                .setCancelText("cancel")
                .setConfirmText("sure")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        DataBaseManager.getInstance().deleteLastRecord();
                        if (currentType == 0) {
                            QueryDataManager.getInstance().updateDayTableList(mYear, mMonth);
                        } else if (currentType == 1) {
                            QueryDataManager.getInstance().updateDayTableList(mYear, mMonth, false);
                        } else if (currentType == 2) {
                            QueryDataManager.getInstance().updateDayTableList(mYear, mMonth, true);
                        }
                        QueryDataManager.getInstance().updateMontTableList(mYear);
                        QueryDataManager.getInstance().updateYearTableList();
                        queryAdapter.notifyDataSetChanged();
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }
    /******************* logic change *********************/
    private void changeAccuracy() {
        queryAdapter.currentAccuracy++;
        if (queryAdapter.currentAccuracy >= Const.Accuracy.values().length) {
            queryAdapter.currentAccuracy = 0;
        } else if (queryAdapter.currentAccuracy < 0) {
            queryAdapter.currentAccuracy = Const.Accuracy.values().length - 1;
        }

        if (queryAdapter.currentAccuracy == Const.Accuracy.day.ordinal()) {
            if (currentType == 0) {
                QueryDataManager.getInstance().updateDayTableList(mYear, mMonth);
            } else {
                QueryDataManager.getInstance().updateDayTableList(mYear, mMonth, currentType != 1);
            }
            queryAdapter = new QueryAdapter(mActivity);
            queryList.setAdapter(queryAdapter);
            queryAdapter.setAccuracy(Const.Accuracy.day.ordinal());
            currentType--;
            changeType();
            includeDayTitle.setVisibility(View.VISIBLE);
            includeMYTitle.setVisibility(View.GONE);
        } else if (queryAdapter.currentAccuracy == Const.Accuracy.month.ordinal()) {
            QueryDataManager.getInstance().updateMontTableList(mYear);
            queryAdapter = new QueryAdapter(mActivity);
            queryList.setAdapter(queryAdapter);
            queryAdapter.setAccuracy(Const.Accuracy.month.ordinal());
            queryAdapter.notifyDataSetChanged();
            includeDayTitle.setVisibility(View.GONE);
            includeMYTitle.setVisibility(View.VISIBLE);
        } else if (queryAdapter.currentAccuracy == Const.Accuracy.year.ordinal()) {
            QueryDataManager.getInstance().updateYearTableList();
            queryAdapter = new QueryAdapter(mActivity);
            queryList.setAdapter(queryAdapter);
            queryAdapter.setAccuracy(Const.Accuracy.year.ordinal());
            queryAdapter.notifyDataSetChanged();
            includeDayTitle.setVisibility(View.GONE);
            includeMYTitle.setVisibility(View.VISIBLE);
        }

    }

    private void changeType() {
        currentType++;
        if (currentType >= dayTableIncomeType.length) {
            currentType = 0;
        } else if (currentType < 0) {
            currentType = dayTableIncomeType.length - 1;
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
    /*************** logic change end ***********************/

    private void chooseDate() {
        if (queryAdapter.currentAccuracy == Const.Accuracy.year.ordinal()) return;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                handleTimeChoose(date);
            }
        }) .setType(new boolean[]{true, queryAdapter.currentAccuracy == Const.Accuracy.day.ordinal(), false, false, false,false})// 默认全部显示
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

    private void print() {
        int[] ydm = new int[] {mYear, mMonth, mDay};
        FileController.getInstance().printTable2SD(Const.TableType.ACCOUNT_DAY, Const.FilterAccuracy.month, ydm);
        FileController.getInstance().printTable2SD(Const.TableType.ACCOUNT_MONTH_AND_YEAR, Const.FilterAccuracy.all_month, ydm);
        FileController.getInstance().printTable2SD(Const.TableType.ACCOUNT_MONTH_AND_YEAR, Const.FilterAccuracy.all_year, ydm);
        TipUtils.showMidToast(mActivity, "ok");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.logo) {
            changeAccuracy();
        } else if (id == R.id.bt_print) {
            DebugLog.d(TAG, "bt print");
            permissionHelper.requestPermissions();
        } else if (id == R.id.delete_last_record) {
            DebugLog.d(TAG, "delete last record");
            popDeleteDialog();
        } else if (id == tv_date.getId()) {
            chooseDate();
        } else if (id == tv_type.getId()) {
            changeType();
            tv_type.setText(dayTableIncomeType[((QueryActivity)mActivity).currentType]);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] mPermissions, int[] grantResults) {
        if (permissionHelper.requestPermissionsResult(requestCode, mPermissions, grantResults)) {
            //权限请求结果，并已经处理了该回调
            return;
        }
        super.onRequestPermissionsResult(requestCode, mPermissions, grantResults);
    }

    @Override
    public int getPermissionsRequestCode() {
        return 0;
    }

    @Override
    public String[] getPermissions() {
        return mPermissions;
    }

    @Override
    public void requestPermissionsSuccess() {
        print();
    }

    @Override
    public void requestPermissionsFail() {

        StringBuilder sb = new StringBuilder();
        mPermissions = PermissionUtil.getDeniedPermissions(this, mPermissions);
        for (String s : mPermissions) {
            if (s.equals(Manifest.permission.CAMERA)) {
                sb.append("相机权限(用于拍照，视频聊天);\n");
            } else if (s.equals(Manifest.permission.RECORD_AUDIO)) {
                sb.append("麦克风权限(用于发语音，语音及视频聊天);\n");
            } else if (s.equals(Manifest.permission.READ_EXTERNAL_STORAGE) || s.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                sb.append("存储,读取权限(用于存储必要信息，缓存数据);\n");
            }
        }
        PermissionUtil.PermissionDialog(this, "程序运行需要如下权限：\n" + sb.toString() + "请在应用权限管理进行设置！");

    }
}
