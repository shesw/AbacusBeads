package com.sheswland.abacusbeads.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PFUtil {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(Const.PF_NANE, Context.MODE_PRIVATE);
        }
        if (editor == null) {
            editor = sharedPreferences.edit();
        }
    }

    public static void putBoolean(Context context, String key, boolean value) {
        init(context);
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        init(context);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

}
