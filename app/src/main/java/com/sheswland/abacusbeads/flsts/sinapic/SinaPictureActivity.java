package com.sheswland.abacusbeads.flsts.sinapic;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sheswland.abacusbeads.R;
import com.sheswland.abacusbeads.utils.SinaConfig;
import com.sheswland.abacusbeads.utils.SinaUtils;
import com.sina.cloudstorage.services.scs.model.ObjectListing;

public class SinaPictureActivity extends AppCompatActivity {

    public static final String TAG = "SinaPictureActivity";

    private Activity mActivity;
    private RecyclerView picList;
    private SinaPicAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_sina_picture);
        mActivity = this;
        findViews();
        initViews();
        requestPics();
    }

    private void findViews(){
        picList = findViewById(R.id.pic_list);
    }

    private void initViews() {
        adapter = new SinaPicAdapter();
        GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
        picList.setLayoutManager(manager);
        picList.setAdapter(adapter);
    }

    private void requestPics() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ObjectListing list = SinaUtils.getInstance().listObjects(SinaConfig.bucketName);
                if (list == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    requestPics();
                    return;
                }
                SinaPicDataController.getInstance().setList(list, SinaConfig.filter_1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}
