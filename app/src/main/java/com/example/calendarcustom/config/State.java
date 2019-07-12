package com.example.calendarcustom.config;

import android.support.annotation.IntDef;

import com.example.calendarcustom.data.CalendarDay;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;


/**
 * Created by rafal.manka on 10/09/2017
 */
@IntDef({
        CalendarDay.DEFAULT,
        CalendarDay.DISABLED,
        CalendarDay.TODAY,
        CalendarDay.SELECTED,
        CalendarDay.UNAVAILABLE,
        CalendarDay.FIRST_SELECTED,
        CalendarDay.LAST_SELECTED,
        CalendarDay.ONLY_SELECTED,
})
@Retention(value = RetentionPolicy.SOURCE)
public @interface State {
}
