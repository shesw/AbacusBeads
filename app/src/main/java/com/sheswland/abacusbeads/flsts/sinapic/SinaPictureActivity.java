package com.sheswland.abacusbeads.flsts.sinapic;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sheswland.abacusbeads.R;
import com.sheswland.abacusbeads.utils.DebugLog;
import com.sheswland.abacusbeads.utils.SinaConfig;
import com.sheswland.abacusbeads.utils.SinaUtils;
import com.sheswland.abacusbeads.utils.TextUtil;
import com.sheswland.abacusbeads.utils.work.WorkManager;
import com.sina.cloudstorage.services.scs.model.ListObjectsRequest;
import com.sina.cloudstorage.services.scs.model.ObjectListing;

public class SinaPictureActivity extends AppCompatActivity {

    public static final String TAG = "SinaPictureActivity";

    private Activity mActivity;
    private RecyclerView picList;
    private SinaPicAdapter adapter;
    private ViewPager previewViewPager;
    private PreviewViewPagerAdapter previewViewPagerAdapter;
    private ObjectListing mObjectListing;

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
        previewViewPager = findViewById(R.id.preview_viewpager);
    }

    private void initViews() {
        previewViewPagerAdapter = new PreviewViewPagerAdapter(this, previewViewPager, picList);
        previewViewPager.setAdapter(previewViewPagerAdapter);

        adapter = new SinaPicAdapter(previewViewPager, picList);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
        picList.setLayoutManager(manager);
        picList.setAdapter(adapter);
    }

    private void requestPics() {
        WorkManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (SinaUtils.getInstance().isReady()) {
                    final ObjectListing list = SinaUtils.getInstance().listObjects(SinaConfig.bucketName);
                    DebugLog.d(TAG, "list.size " + list.getObjectSummaries().size());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SinaPicDataController.getInstance().addList(list, SinaConfig.filter_1, SinaConfig.filter_2, SinaConfig.filter_3, SinaConfig.filter_4);
                            adapter.notifyDataSetChanged();
                            previewViewPagerAdapter.notifyDataSetChanged();
                            mObjectListing = list;
                        }
                    });
                }
            }
        });
    }

    private void requestNextBatchOfPics() {
        WorkManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                mObjectListing.setMaxKeys(400);
                final ObjectListing list = SinaUtils.getInstance().listNextBatchOfObjects(mObjectListing);
                DebugLog.d(TAG, "listing " + list.getObjectSummaries().size());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SinaPicDataController.getInstance().addList(list, SinaConfig.filter_1, SinaConfig.filter_2, SinaConfig.filter_3, SinaConfig.filter_4);
                        adapter.notifyDataSetChanged();
                        previewViewPagerAdapter.notifyDataSetChanged();
                        mObjectListing = list;
                    }
                });
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (picList.getVisibility() != View.VISIBLE) {
            previewViewPager.setVisibility(View.GONE);
            picList.setVisibility(View.VISIBLE);
            return;
        }
        finish();
    }
}
