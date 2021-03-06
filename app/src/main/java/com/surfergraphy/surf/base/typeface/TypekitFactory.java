package com.surfergraphy.surf.base.typeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surfergraphy.surf.R;

/**
 * Created by ddfactory on 2017-11-22.
 */

public class TypekitFactory {
    public TypekitFactory() {
    }

    public View onViewCreated(View view, String name, View parent, Context context, AttributeSet attrs) {
        if (view == null) {
            return null;
        }

        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            applyFontForTextView(context, attrs, textView);

        } else if (view instanceof ViewGroup) {
            applyFontForViewGroup(context, attrs, (ViewGroup) view);
        }

        return view;
    }

    public View onViewCreated(View view, Context context, AttributeSet attrs) {

        return view;
    }

    void applyFontForViewGroup(Context context, AttributeSet attrs, ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView) {
                applyFontForTextView(context, attrs, (TextView) child);
            } else if (child instanceof ViewGroup)
                applyFontForViewGroup(context, attrs, (ViewGroup) child);
        }
    }

    private void applyFontForTextView(Context context, AttributeSet attrs, TextView textView) {
        if (textView.getTag(R.id.Typekit) != null) return;

        Typekit typekit = Typekit.getInstance();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Typekit);
        String fontKey = (array != null) ? array.getString(R.styleable.Typekit_fontType) : null;

        if (textView.getText().toString().equals("더보기"))
            Log.d("더보기","more");
        if (!TextUtils.isEmpty(fontKey)) {
            textView.setTypeface(typekit.get(fontKey));
        } else {
            Typeface typeface = textView.getTypeface();
            if (typeface == null || (!typeface.isBold() && !typeface.isItalic())) {
                textView.setTypeface(typekit.get(Typekit.Style.normal), Typeface.NORMAL);
            } else if (typeface.isBold() && typeface.isItalic()) {
                textView.setTypeface(typekit.get(Typekit.Style.bold_italic), Typeface.BOLD_ITALIC);
            } else if (typeface.isBold()) {
                textView.setTypeface(typekit.get(Typekit.Style.bold), Typeface.BOLD);
            } else {
                textView.setTypeface(typekit.get(Typekit.Style.italic), Typeface.ITALIC);
            }
            if (textView.getText() instanceof Spanned) {
                Spanned spanned = (Spanned) textView.getText();
                StyleSpan[] spans = spanned.getSpans(0, spanned.length(), StyleSpan.class);

                SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText());
                builder.setSpan(new TypekitSpan(Typeface.NORMAL, typekit), 0, spanned.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

                if (spans != null && spans.length > 0) {
                    for (StyleSpan styleSpan : spans) {
                        int start = spanned.getSpanStart(styleSpan);
                        int end = spanned.getSpanEnd(styleSpan);
                        if (styleSpan.getStyle() == Typeface.BOLD) {
                            builder.setSpan(new TypekitSpan(Typeface.BOLD, typekit), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        } else if (styleSpan.getStyle() == Typeface.BOLD_ITALIC) {
                            builder.setSpan(new TypekitSpan(Typeface.BOLD_ITALIC, typekit), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        } else if (styleSpan.getStyle() == Typeface.ITALIC) {
                            builder.setSpan(new TypekitSpan(Typeface.ITALIC, typekit), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        }
                    }
                }
                textView.setText(builder);
            }
        }

        if (array != null) {
            array.recycle();
        }

        textView.setTag(R.id.Typekit, true);
    }
}
