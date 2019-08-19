package com.sheswland.abacusbeads;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

public class OperationActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private final String TAG = "OperationActivity";

    private Activity mActivity;

    private EditText inputDate;
    private EditText inputContent;
    private EditText inputSpend;
    private RadioGroup radioGroup;
    private Button btCommit;
    private Button btQuery;
    private Button btReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;

        findViews();
        initViews();
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
        radioGroup.setOnCheckedChangeListener(this);
        btCommit.setOnClickListener(this);
        btQuery.setOnClickListener(this);
        btReset.setOnClickListener(this);
    }



    /************** implement interface methods ***************/
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_commit) {

        } else if (id == R.id.bt_query) {

        } else if (id == R.id.bt_reset) {

        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_spend:
                break;
            case R.id.rb_income:
                break;
        }
    }
}
