package com.example.calendarcustom.callback;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

public interface ResProvider {
    @Nullable
    Typeface getCustomFont();

    @StyleRes
    int getMonthTitleStyle();

    @StyleRes
    int getLegendItemStyle();

    @StyleRes
    int getCurrentDayStyle();

    @StyleRes
    int getSelectedDayStyle();

    @StyleRes
    int getSelectedBeginningDayStyle();

    @StyleRes
    int getSelectedMiddleDayStyle();

    @StyleRes
    int getSelectedEndDayStyle();

    @StyleRes
    int getUnavailableItemStyle();

    @StyleRes
    int getDayStyle();

    @StyleRes
    int getDisabledItemStyle();

    boolean showYearAlways();

    boolean softLineBreaks();
}
