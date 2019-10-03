package com.sheswland.abacusbeads.flsts.sinapic;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sheswland.abacusbeads.R;
import com.sheswland.abacusbeads.utils.DebugLog;
import com.sheswland.abacusbeads.utils.SinaUtils;

import java.util.List;

public class PreviewViewPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private Activity mActivity;
    private ViewPager mViewPager;
    private RecyclerView mRecyclerView;

    public PreviewViewPagerAdapter(Activity activity, ViewPager viewPager, RecyclerView recyclerView) {
        mActivity = activity;
        mViewPager = viewPager;
        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.sina_pic_preview, container, false);
        SimpleDraweeView simpleDraweeView = view.findViewById(R.id.preview_pic);
        String url = SinaUtils.getInstance().generateUrlByDefault(SinaPicDataController.getInstance().getPath(position));
        DebugLog.d(SinaPictureActivity.TAG, "instantateItem url " + url);
        simpleDraweeView.setImageURI(Uri.parse(url));
        simpleDraweeView.setTag(position);
        simpleDraweeView.setOnClickListener(this);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return SinaPicDataController.getInstance().getCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();
        DebugLog.d(SinaPictureActivity.TAG, index + "");
        mViewPager.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
