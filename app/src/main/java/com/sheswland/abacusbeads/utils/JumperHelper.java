package com.sheswland.abacusbeads.utils;

import android.app.Activity;
import android.content.Intent;

import com.sheswland.abacusbeads.flsts.FlstsMainActivity;
import com.sheswland.abacusbeads.flsts.sinapic.SinaPictureActivity;
import com.sheswland.abacusbeads.operate.OperationActivity;
import com.sheswland.abacusbeads.query.QueryActivity;


public class JumperHelper {
    private static String TAG = "JumperHelper";

    public static void jump2Operate(Activity activity) {
        Intent intent = new Intent(activity, OperationActivity.class);
        activity.startActivity(intent);
    }

    public static void jump2Query(Activity activity) {
        Intent intent = new Intent(activity, QueryActivity.class);
        activity.startActivity(intent);
    }

    public static void jump2Flsts(Activity activity) {
        Intent intent = new Intent(activity, FlstsMainActivity.class);
        activity.startActivity(intent);
    }

    public static void jump2SinaPicQuery(Activity activity) {
        Intent intent = new Intent(activity, SinaPictureActivity.class);
        activity.startActivity(intent);
    }
}
