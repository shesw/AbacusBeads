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
    public void addList(ObjectListing list) {
        addList(list, "");
    }
    public void addList(ObjectListing list, String... filters) {
        for (S3ObjectSummary s3ObjectSummary : list.getObjectSummaries()) {

            boolean inFilter = false;
            if (filters.length > 0) {
                for (String filter : filters) {
                    inFilter |= s3ObjectSummary.getKey().contains(filter);
                }
            }

            if (TextUtil.isImage(s3ObjectSummary.getKey()) && inFilter) {
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
