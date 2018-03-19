package com.surfergraphy.surf.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ddfactory on 2018-02-25.
 */

public class CommonTask {
    public static final String TAG = "CommonTask";

    public static PackageInfo getPackageInfo(final Context context, int flag) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), flag);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Unable to get PackageInfo", e);
        }
        return null;
    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md1 = MessageDigest.getInstance("SHA1");
                md1.update(signature.toByteArray());
                String key = Base64.encodeToString(md1.digest(), Base64.NO_WRAP);
                Log.d("KEY_HASH", key);
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }
}
