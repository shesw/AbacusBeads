package com.sheswland.abacusbeads.flsts.sinapic;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class PreviewViewPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private Activity mActivity;
    private ViewPager mViewPager;

    public PreviewViewPagerAdapter(Activity activity, ViewPager viewPager) {
        mActivity = activity;
        mViewPager = viewPager;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(container.getContext());
        simpleDraweeView.setImageURI(Uri.parse(SinaPicDataController.getInstance().getPath(position)));
        simpleDraweeView.setTag(position);
        simpleDraweeView.setOnClickListener(this);
        return simpleDraweeView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
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
        mViewPager.setVisibility(View.GONE);
    }
}
