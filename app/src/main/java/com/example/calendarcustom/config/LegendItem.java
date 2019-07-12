package com.example.calendarcustom.config;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.calendarcustom.callback.ResProvider;
import com.example.calendarcustom.R;

import java.text.DateFormatSymbols;
import java.util.Arrays;

public class LegendItem {
    private static final String[] days;

    private static final int[] attrs = {
            android.R.attr.textColor,
            android.R.attr.textSize,
            android.R.attr.padding,
            android.R.attr.gravity
    };

    /**
     * get title day in week
     */
    static {
        Arrays.sort(attrs);
        String[] original = new DateFormatSymbols().getShortWeekdays();
        days = new String[original.length];
        for (int i = 0; i < days.length; i++) {
            if (!original[i].trim().isEmpty() && original[i] != null) {
                if (i == 1) {
                    days[days.length - 1] = original[i];
                } else {
                    days[i - 1] = original[i];
                }
            }
        }
    }

    private final int dayOfWeek;

    @Nullable
    private TextView textView;

    public LegendItem(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * custom view
     * @param parent
     * @param resProvider
     * @return
     */
    public View layout(LinearLayout parent, ResProvider resProvider) {
        if (textView == null) {
            textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.v_scroll_day_legend, parent, false);

            TypedArray typedArray = textView.getContext().getTheme().obtainStyledAttributes(resProvider.getLegendItemStyle(), attrs);
            for (int i = 0; i < attrs.length; i++) {
                switch (attrs[i]) {
                    case android.R.attr.textColor:
                        textView.setTextColor(typedArray.getColor(i, ContextCompat.getColor(textView.getContext(), android.R.color.black)));
                        break;
                    case android.R.attr.textSize:
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(i, 16));
                        break;
                    case android.R.attr.gravity:
                        textView.setGravity(typedArray.getInt(i, Gravity.CENTER));
                        break;
                    case android.R.attr.padding:
                        int padding = typedArray.getDimensionPixelOffset(i, 0);
                        textView.setPadding(padding, padding, padding, padding);
                        break;
                    default:
                        break;
                }
            }

            typedArray.recycle();
            Typeface typeface = resProvider.getCustomFont();
            if (typeface != null) {
                textView.setTypeface(typeface);
            }
        }
        return textView;
    }

    /**
     * show day
     */
    public void display() {
        if (textView != null) {
            textView.setText(getReadableSymbol());
        }
    }

    private String getReadableSymbol() {
        return String.valueOf(days[dayOfWeek]);
    }
}
