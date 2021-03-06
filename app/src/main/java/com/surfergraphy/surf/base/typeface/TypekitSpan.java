package com.surfergraphy.surf.base.typeface;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.StyleSpan;

/**
 * Created by ddfactory on 2017-11-22.
 */

public class TypekitSpan extends StyleSpan {
    private final Typekit mTypekit;

    public TypekitSpan(int style, Typekit typekit) {
        super(style);
        mTypekit = typekit;
    }

    public TypekitSpan(Parcel src, Typekit typekit) {
        super(src);
        mTypekit = typekit;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        apply(ds, getStyle(), mTypekit);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        apply(paint, getStyle(), mTypekit);
    }

    private static void apply(Paint paint, int style, Typekit typekit) {
        int oldStyle;

        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        @SuppressLint("WrongConstant") int want = oldStyle | style;



        Typeface tf = null;

        switch (want) {
            case Typeface.BOLD:
                tf = typekit.get(Typekit.Style.bold);
                break;
            case Typeface.ITALIC:
                tf = typekit.get(Typekit.Style.italic);
                break;
            case Typeface.BOLD_ITALIC:
                tf = typekit.get(Typekit.Style.bold_italic);
                break;
            default:
                tf = null;
                break;
        }

        if (tf == null) {
            if (old == null) {
                tf = Typeface.defaultFromStyle(want);
            } else {
                tf = Typeface.create(old, want);
            }

            int fake = want & ~tf.getStyle();

            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }
        }

        paint.setTypeface(tf);
    }
}