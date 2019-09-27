package com.sheswland.abacusbeads.utils;

import android.app.Activity;
import android.content.Intent;

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
}
