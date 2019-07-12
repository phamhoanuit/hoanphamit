package com.example.calendarcustom.callback;

import android.support.annotation.NonNull;

import com.example.calendarcustom.data.CalendarDay;
import com.example.calendarcustom.data.CalendarMonth;

public interface ClickCallback {
        void onCalendarDayClicked(@NonNull CalendarMonth calendarMonth, @NonNull CalendarDay calendarDay);
}
