package com.sheswland.abacusbeads.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class TipUtils {
    private static Toast mToast;

    public static void showMidToast(Context context, String string) {
        mToast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
}
