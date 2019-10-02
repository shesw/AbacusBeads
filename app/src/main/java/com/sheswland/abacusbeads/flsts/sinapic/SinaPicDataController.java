package com.sheswland.abacusbeads.flsts.sinapic;

import com.sheswland.abacusbeads.utils.DebugLog;
import com.sheswland.abacusbeads.utils.TextUtil;
import com.sina.cloudstorage.services.scs.model.ObjectListing;
import com.sina.cloudstorage.services.scs.model.S3ObjectSummary;

import java.util.ArrayList;
import java.util.List;

public class SinaPicDataController {

    private static SinaPicDataController _holder;
    public static SinaPicDataController getInstance() {
        if (_holder == null) {
            _holder = new SinaPicDataController();
        }
        return _holder;
    }

    private List<String> mList = new ArrayList<>();
    public void setList(ObjectListing list) {
        setList(list, "");
    }
    public void setList(ObjectListing list, String filter) {
        for (S3ObjectSummary s3ObjectSummary : list.getObjectSummaries()) {
            if (TextUtil.isImage(s3ObjectSummary.getKey()) && s3ObjectSummary.getKey().contains(filter)) {
                DebugLog.d(SinaPictureActivity.TAG, "key " + s3ObjectSummary.getKey());
                mList.add(s3ObjectSummary.getKey());
            }
        }
    }

    public String getPath(int index) {
        return  mList.get(index);
    }
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

}
