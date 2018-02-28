package com.surfergraphy.surf.base.typeface;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ddfactory on 2017-11-22.
 */

public class Typekit {
    public enum Style {
        normal, italic, light, light_italic, bold, bold_italic, black, black_italic, custom1, custom2, custom3, custom4, custom5, custom6, custom7, custom8, custom9;
    }

    private static Typekit ourInstance = new Typekit();

    public static Typekit getInstance() {
        return ourInstance;
    }

    private Map<String, Typeface> mFonts = new HashMap<>();
    private Typekit() {
    }

    public Typeface get(Style style){
        return get(style.toString());
    }

    public Typeface get(String key){
        return mFonts.get(key);
    }

    public Typekit addNormal(Typeface typeface){
        mFonts.put(Style.normal.toString(), typeface);
        return this;
    }

    public Typekit addItalic(Typeface typeface){
        mFonts.put(Style.italic.toString(), typeface);
        return this;
    }

    public Typekit addLight(Typeface typeface){
        mFonts.put(Style.light.toString(), typeface);
        return this;
    }

    public Typekit addLightItalic(Typeface typeface){
        mFonts.put(Style.light_italic.toString(), typeface);
        return this;
    }

    public Typekit addBold(Typeface typeface){
        mFonts.put(Style.bold.toString(), typeface);
        return this;
    }

    public Typekit addBoldItalic(Typeface typeface){
        mFonts.put(Style.bold_italic.toString(), typeface);
        return this;
    }

    public Typekit addBlack(Typeface typeface){
        mFonts.put(Style.black.toString(), typeface);
        return this;
    }

    public Typekit addBlackItalic(Typeface typeface){
        mFonts.put(Style.black_italic.toString(), typeface);
        return this;
    }

    public Typekit addCustom1(Typeface typeface){
        mFonts.put(Style.custom1.toString(), typeface);
        return this;
    }

    public Typekit addCustom2(Typeface typeface){
        mFonts.put(Style.custom2.toString(), typeface);
        return this;
    }

    public Typekit addCustom3(Typeface typeface){
        mFonts.put(Style.custom3.toString(), typeface);
        return this;
    }

    public Typekit addCustom4(Typeface typeface){
        mFonts.put(Style.custom4.toString(), typeface);
        return this;
    }

    public Typekit addCustom5(Typeface typeface){
        mFonts.put(Style.custom5.toString(), typeface);
        return this;
    }

    public Typekit addCustom6(Typeface typeface){
        mFonts.put(Style.custom6.toString(), typeface);
        return this;
    }

    public Typekit addCustom7(Typeface typeface){
        mFonts.put(Style.custom7.toString(), typeface);
        return this;
    }

    public Typekit addCustom8(Typeface typeface){
        mFonts.put(Style.custom8.toString(), typeface);
        return this;
    }

    public Typekit addCustom9(Typeface typeface){
        mFonts.put(Style.custom9.toString(), typeface);
        return this;
    }

    public Typekit add(String key, Typeface typeface) {
        mFonts.put(key, typeface);
        return this;
    }

    public static Typeface createFromAsset(Context context, String path){
        return Typeface.createFromAsset(context.getAssets(), path);
    }

}
