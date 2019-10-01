package com.sheswland.abacusbeads.utils;

import java.text.DecimalFormat;

public class TextUtil {
    private static DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public static boolean isEmpty(String string) {
        return "".equals(string) || null == string;
    }

    public static String formatNumber2xx(int i) {
        return i > 9 ? String.valueOf(i) : "0" + i;
    }

    public static Float formatFloat2(float f) {
        return Float.valueOf(decimalFormat.format(f));
    }

    public static String getExtensionWithDot(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

    public static boolean isImage (String url) {
        String extension = getExtensionWithDot(url);
        return ".jpeg".equals(extension) || ".jpg".equals(extension) || ".png".equals(extension);
    }
}
