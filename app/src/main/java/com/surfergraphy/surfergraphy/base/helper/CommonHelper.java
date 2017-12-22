package com.surfergraphy.surfergraphy.base.helper;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

public class CommonHelper {

    public static void showMessageOK(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message).setNeutralButton("OK", null).create().show();
    }
}
