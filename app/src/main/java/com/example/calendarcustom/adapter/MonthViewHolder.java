package com.example.calendarcustom.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.calendarcustom.callback.ClickCallback;
import com.example.calendarcustom.R;
import com.example.calendarcustom.data.CalendarDay;
import com.example.calendarcustom.data.CalendarMonth;
import com.example.calendarcustom.style.DayResProvider;
import com.example.calendarcustom.style.MonthResProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthViewHolder extends RecyclerView.ViewHolder {

    @Nullable
    private final TextView title;
    private MonthResProvider monthResProvider;

    private final WeekHolder[] weeks = new WeekHolder[7];
    private boolean textAllCaps;


    private MonthViewHolder(@NonNull View rootView, @NonNull ClickCallback calendarCallback, @NonNull MonthResProvider monthResProvider, @NonNull DayResProvider dayResProvider) {
        super(rootView);
        this.monthResProvider = monthResProvider;
        LinearLayout monthContainer = rootView.findViewById(R.id.monthContainer);
        title = rootView.findViewById(R.id.title);
        setupTitleAppearance(monthResProvider);

        for (int i = 0; i < weeks.length; i++) {
            WeekHolder holder = new WeekHolder(calendarCallback, dayResProvider);
            weeks[i] = holder;
            monthContainer.addView(holder.layout(monthContainer));
        }
    }

    private void setupTitleAppearance(@NonNull MonthResProvider resProvider) {
        if (title == null) {
            return;
        }
        title.setTextColor(resProvider.getTextColor());
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, resProvider.getTextSize());
        title.setGravity(resProvider.getGravity());
        title.setTypeface(title.getTypeface(), resProvider.getTextStyle());
        textAllCaps = resProvider.getTextAllCaps();
    }

    MonthViewHolder(View rootView) {
        super(rootView);
        title = null;
    }

    static MonthViewHolder create(@NonNull ViewGroup parent, @NonNull ClickCallback calendarCallback, @NonNull MonthResProvider resProvider, @NonNull DayResProvider dayResProvider) {
        return new MonthViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.v_scroll_month, parent, false),
                calendarCallback, resProvider, dayResProvider);
    }

    void bind(CalendarMonth month) {
        if (title != null) {
            String txt = monthResProvider.showYearAlways() ? month.getMonthNameWithYear() : month.getReadableMonthName();
            txt = txt.substring(0,1).toUpperCase() + txt.substring(1);
            title.setText(applyCase(txt));
        }
        for (int i = 0; i <= weeks.length - 1; i++) {
            weeks[i].display(i, month, filterWeekDays(i, month));
        }
    }

    private String applyCase(@NonNull String string) {
        return textAllCaps ? string.toUpperCase() : string;
    }

    CalendarDay[] filterWeekDays(int weekOfMonth, CalendarMonth calendarMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.YEAR, calendarMonth.getYear());
        calendar.set(Calendar.MONTH, calendarMonth.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        List<CalendarDay> days = new ArrayList<>();
        for (CalendarDay calendarDay : calendarMonth.getDays()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendarDay.getDay());
            if (calendar.get(Calendar.WEEK_OF_MONTH) == weekOfMonth) {
                days.add(calendarDay);
            }
        }
        return days.toArray(new CalendarDay[days.size()]);
    }
}
