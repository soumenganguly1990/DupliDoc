package com.soumen.duplidoc.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.soumen.duplidoc.extras.AppCommonValues;

/**
 * Created by Soumen on 10/16/2017.
 */
public class HelveticaBoldTextView extends TextView {

    public HelveticaBoldTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public HelveticaBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public HelveticaBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(AppCommonValues.HELVETICAROUNDEDBOLD, context);
        setTypeface(customFont);
    }
}