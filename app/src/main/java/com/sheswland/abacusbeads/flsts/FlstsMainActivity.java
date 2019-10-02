package com.sheswland.abacusbeads.flsts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sheswland.abacusbeads.R;
import com.sheswland.abacusbeads.utils.JumperHelper;

public class FlstsMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flsts_main);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.bt_potential_1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_potential_1) {
            JumperHelper.jump2SinaPicQuery(this);
        }
    }
}
