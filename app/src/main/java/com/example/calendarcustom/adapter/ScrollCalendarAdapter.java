package com.example.calendarcustom.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.calendarcustom.Config;
import com.example.calendarcustom.callback.ClickCallback;
import com.example.calendarcustom.callback.DateWatcher;
import com.example.calendarcustom.callback.MonthScrollListener;
import com.example.calendarcustom.callback.OnDateClickListener;
import com.example.calendarcustom.config.State;
import com.example.calendarcustom.data.CalendarDay;
import com.example.calendarcustom.data.CalendarMonth;
import com.example.calendarcustom.style.DayResProvider;
import com.example.calendarcustom.style.MonthResProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScrollCalendarAdapter extends RecyclerView.Adapter<MonthViewHolder> implements ClickCallback {

    @NonNull
    private final List<CalendarMonth> months = new ArrayList<>();
    @Nullable
    private RecyclerView recyclerView;
    @Nullable
    private MonthScrollListener monthScrollListener;
    @Nullable
    private OnDateClickListener onDateClickListener;
    @Nullable
    private DateWatcher dateWatcher;
    private MonthResProvider monthResProvider;
    private DayResProvider dayResProvider;

    public ScrollCalendarAdapter(@NonNull MonthResProvider monthResProvider, @NonNull DayResProvider dayResProvider) {
        this.monthResProvider = monthResProvider;
        this.dayResProvider = dayResProvider;
        months.add(CalendarMonth.now());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MonthViewHolder.create(parent, this, monthResProvider, dayResProvider);
    }

    @Override
    public void onBindViewHolder(MonthViewHolder holder, int position) {
        CalendarMonth month = getItem(position);
        prepare(month);
        holder.bind(month);
        afterBindViewHolder(position);
    }

    private void afterBindViewHolder(int position) {
        if (recyclerView != null) {
            if (shouldAddNextMonth(position)) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        appendCalendarMonth();
                    }
                });
            }
        }
    }

    private boolean shouldAddNextMonth(int position) {
        return isNearBottom(position) && isAllowedToAddNextMonth();
    }

    protected boolean isAllowedToAddNextMonth() {
        CalendarMonth item = getLastItem();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, Config.MONTH_ADD);
        if (monthScrollListener == null) {
            return true;
        }
        if (item.getMonth() == calendar.get(Calendar.MONTH) || Config.MONTH_ADD == 0) {
            return false;
        }
        return monthScrollListener.shouldAddNextMonth(item.getYear(), item.getMonth());
    }

    private void prepare(CalendarMonth month) {
        for (CalendarDay calendarDay : month.getDays()) {
            calendarDay.setState(makeState(month, calendarDay));
        }
    }

    @State
    private int makeState(CalendarMonth month, CalendarDay calendarDay) {
        int year = month.getYear();
        int monthInt = month.getMonth();
        int day = calendarDay.getDay();
        return getStateForDate(year, monthInt, day);
    }

    @State
    protected int getStateForDate(int year, int month, int day) {
        if (dateWatcher == null) {
            return CalendarDay.DEFAULT;
        }
        return dateWatcher.getStateForDate(year, month, day);
    }

    private void appendCalendarMonth() {
        months.add(getLastItem().next());
        notifyItemInserted(months.size() - 1);
    }

    private CalendarMonth getFirstItem() {
        return months.get(0);
    }

    private CalendarMonth getLastItem() {
        return months.get(months.size() - 1);
    }

    private CalendarMonth getItem(int position) {
        return months.get(position);
    }

    private boolean isNearTop(int position) {
        return position == 0;
    }

    private boolean isNearBottom(int position) {
        return months.size() - 1 <= position;
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    public void setOnDateClickListener(@Nullable OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }

    public void setMonthScrollListener(@Nullable MonthScrollListener monthScrollListener) {
        this.monthScrollListener = monthScrollListener;
    }

    public void setDateWatcher(@Nullable DateWatcher dateWatcher) {
        this.dateWatcher = dateWatcher;
    }

    @Override
    public void onCalendarDayClicked(@NonNull CalendarMonth calendarMonth, @NonNull CalendarDay calendarDay) {
        int year = calendarMonth.getYear();
        int month = calendarMonth.getMonth();
        int day = calendarDay.getDay();
        onCalendarDayClicked(year, month, day);
        notifyDataSetChanged();
    }

    protected void onCalendarDayClicked(int year, int month, int day) {
        if (onDateClickListener != null) {
            onDateClickListener.onCalendarDayClicked(year, month, day);
        }
    }

}
