package com.sheswland.abacusbeads.flsts.sinapic;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sheswland.abacusbeads.R;
import com.sheswland.abacusbeads.utils.DebugLog;
import com.sheswland.abacusbeads.utils.SinaUtils;

public class SinaPicAdapter extends RecyclerView.Adapter<SinaPicAdapter.MyViewHolder> implements View.OnClickListener {

    private ViewPager mPreViewViewPager;

    public SinaPicAdapter(ViewPager previewViewPager) {
        mPreViewViewPager = previewViewPager;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sina_pic_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String url = SinaUtils.getInstance().generateUrlByDefault(SinaPicDataController.getInstance().getPath(i));
        DebugLog.d(SinaPictureActivity.TAG, "url " );
        myViewHolder.pic.setImageURI(Uri.decode(url));
        myViewHolder.pic.setTag(i);
        myViewHolder.pic.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return SinaPicDataController.getInstance().getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView pic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
        }
    }

    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();
        DebugLog.d(SinaPictureActivity.TAG, "onClick " + index);
        mPreViewViewPager.setCurrentItem(index);
        mPreViewViewPager.setVisibility(View.VISIBLE);
    }
}
