package com.surfergraphy.surf.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.surfergraphy.surf.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

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

    public static String getRandomString(int length)
    {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();

        String chars[] = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z".split(",");

        for (int i=0 ; i<length ; i++)
        {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }

    public static void savePicture(Context context, String photoUrl, boolean isIncludeWatermark) {
        if (TextUtils.isEmpty(photoUrl))
            return;
        new Thread() {
            public void run() {
                try {
                    URL imageUrl = new URL(photoUrl);
                    Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream()).copy(Bitmap.Config.ARGB_8888, true);
                    String extStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Surfergraphy/";
                    File directory = new File(extStorageDirectory);
                    if (!directory.exists())
                        directory.mkdirs();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
                    Date now = new Date();
                    File file = new File(extStorageDirectory, formatter.format(now) + "_" + getRandomString(4) + ".png");
                    if (isIncludeWatermark) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setDither(true);
                        paint.setAlpha(160);
                        paint.setFilterBitmap(true);

                        Canvas canvas = new Canvas(bitmap);

                        Bitmap originWatermark = BitmapFactory.decodeResource(context.getResources(), R.drawable.surfergraphy_final_white);
                        int resizeWidth = (int) (bitmap.getWidth() * 0.8);
                        int width = originWatermark.getWidth();
                        int height = originWatermark.getHeight();
                        Bitmap resizedWatermark = Bitmap.createScaledBitmap(originWatermark, resizeWidth, height * resizeWidth / width, true);

                        Matrix matrix = new Matrix();
                        matrix.postTranslate(canvas.getWidth() / 2 - resizedWatermark.getWidth() / 2, canvas.getHeight() / 2 - resizedWatermark.getHeight() / 2);

                        canvas.drawBitmap(resizedWatermark, matrix, paint);
                    }
                    OutputStream outStream = null;
                    outStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                    outStream.flush();
                    outStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
