package com.sheswland.abacusbeads.sinapic;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sheswland.abacusbeads.R;
import com.sheswland.abacusbeads.utils.DebugLog;
import com.sheswland.abacusbeads.utils.SinaConfig;
import com.sheswland.abacusbeads.utils.SinaUtils;
import com.sheswland.abacusbeads.utils.TextUtil;
import com.sina.cloudstorage.auth.AWSCredentials;
import com.sina.cloudstorage.auth.BasicAWSCredentials;
import com.sina.cloudstorage.services.scs.SCS;
import com.sina.cloudstorage.services.scs.SCSClient;
import com.sina.cloudstorage.services.scs.model.ObjectListing;

import java.net.URL;
import java.util.Date;

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
                SinaPicDataController.getInstance().setList(list);
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
