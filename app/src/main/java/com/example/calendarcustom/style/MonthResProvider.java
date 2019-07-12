package com.example.calendarcustom.style;

import android.support.annotation.ColorInt;

public interface MonthResProvider {
    @ColorInt
    int getTextColor();

    float getTextSize();

    int getGravity();

    boolean getTextAllCaps();

    boolean showYearAlways();

    int getTextStyle();
}

