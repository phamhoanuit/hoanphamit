package com.example.calendarcustom.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.calendarcustom.callback.DateWatcher;
import com.example.calendarcustom.callback.MonthScrollListener;
import com.example.calendarcustom.callback.OnDateClickListener;
import com.example.calendarcustom.R;
import com.example.calendarcustom.config.ScrollCalendar;
import com.example.calendarcustom.config.State;
import com.example.calendarcustom.data.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    SeekBar skbReceiveCar, skbReturnCar;
    View viewReceive, viewReturn;
    TextView txtTimeReceive, txtTimeReturn, txtHourReturn, txtHourReceive;
    String time_hour = "";
    String time_minutes = "";

    // custom calendar
    ScrollCalendar calendar;
    TextView txtDayReceive, txtDayReturn;
    @Nullable
    private Calendar from;
    @Nullable
    private Calendar until;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        viewReceive = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_seekbar_thumb, null, false);
        viewReturn = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_seekbar_thumb_1, null, false);
        txtTimeReceive = viewReceive.findViewById(R.id.tvProgressReceive);
        txtTimeReturn = viewReturn.findViewById(R.id.tvProgressReturn);
        txtDayReceive = findViewById(R.id.day_receive);
        txtDayReturn = findViewById(R.id.day_return);
        viewReceive.bringToFront();
        viewReturn.bringToFront();
        txtTimeReceive.setEnabled(true);
        txtTimeReturn.setEnabled(true);

        if (txtDayReceive.getText().toString().trim().length() == 0) {
            txtDayReceive.setText(getDayFormat(new Date()));
        }
        if (txtDayReturn.getText().toString().trim().length() == 0) {
            txtDayReturn.setText(getString(R.string.day_return));
        }
        seekBarChangeListener(skbReceiveCar, txtTimeReceive, txtHourReceive);
        seekBarChangeListener(skbReturnCar, txtTimeReturn, txtHourReturn);
        customCalendar();
    }


    @SuppressLint("SimpleDateFormat")
    private String getDayFormat(Date date) {
        return new SimpleDateFormat("EEE, dd/MM/YYYY").format(date);
    }

    private void customCalendar() {
        calendar = findViewById(R.id.scrollCalendar);
        if (calendar == null) {
            return;
        }
        calendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onCalendarDayClicked(int year, int month, int day) {
                doOnCalendarDayClicked(year, month, day);
            }
        });
        calendar.setDateWatcher(new DateWatcher() {
            @State
            @Override
            public int getStateForDate(int year, int month, int day) {
                return doGetStateForDate(year, month, day);
            }
        });
        calendar.setMonthScrollListener(new MonthScrollListener() {
            @Override
            public boolean shouldAddNextMonth(int lastDisplayedYear, int lastDisplayedMonth) {
                return doShouldAddNextMonth(lastDisplayedYear, lastDisplayedMonth);
            }

            @Override
            public boolean shouldAddPreviousMonth(int firstDisplayedYear, int firstDisplayedMonth) {
                return false;
            }
        });
    }

    private void initView() {
        skbReceiveCar = findViewById(R.id.time_receive_car);
        skbReturnCar = findViewById(R.id.time_return_car);
        txtHourReceive = findViewById(R.id.hour_receive);
        txtHourReturn = findViewById(R.id.hour_return);
        txtHourReturn.setText("00:00");
        txtHourReceive.setText("00:00");
    }

    private void seekBarChangeListener(SeekBar seekBar, final TextView txtTime, final TextView txtHour) {
        seekBar.setMax(24 * 2);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int hour = progress / 2;
                int minutes = (progress % 2) * 30;
                if (hour < 10 && minutes == 0) {
                    time_hour = "0" + hour;
                    time_minutes = 0 + "0";
                } else if (hour >= 10 && minutes != 0) {
                    time_hour = String.valueOf(hour);
                    time_minutes = String.valueOf(minutes);
                } else {
                    if (hour < 10) {
                        time_hour = "0" + hour;
                        time_minutes = String.valueOf(minutes);
                    } else if (minutes == 0) {
                        time_minutes = 0 + "0";
                        time_hour = String.valueOf(hour);
                    }
                }
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                txtTime.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
                txtTime.setTextColor(Color.WHITE);
                txtTime.setText(time_hour + ":" + time_minutes);
                txtHour.setText(time_hour + ":" + time_minutes);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                txtTime.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtTime.setText(time_hour + ":" + time_minutes);
            }
        });
    }

    private boolean doShouldAddNextMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 10);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long target = calendar.getTimeInMillis();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);

        return calendar.getTimeInMillis() < target;
    }

    @State
    private int doGetStateForDate(int year, int month, int day) {
        Calendar calendarDayInMonth = Calendar.getInstance();
        calendarDayInMonth.set(year, month, day);
        long timeDayInMonth = calendarDayInMonth.getTimeInMillis();
        if (getCurrentTimeMillies() > timeDayInMonth) {
            return CalendarDay.UNAVAILABLE;
        }
        if (isSelected(from, year, month, day)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(from.get(Calendar.YEAR), from.get(Calendar.MONTH), from.get(Calendar.DAY_OF_MONTH));
            txtDayReceive.setText(getDayFormat(calendar.getTime()));
            txtDayReturn.setText(getString(R.string.day_return));
            return CalendarDay.SELECTED;
        }
        if (isInRange(from, until, year, month, day)) {
            return CalendarDay.SELECTED;
        }
        if (isToday(year, month, day)) {
            return CalendarDay.TODAY;
        }

        return CalendarDay.DEFAULT;
    }

    private boolean isToday(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long milliesCurrent = calendar.getTime().getTime();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        long millies = calendar.getTimeInMillis();

        return milliesCurrent == millies;
    }

    private long getCurrentTimeMillies() {
        return Calendar.getInstance().getTimeInMillis();
    }

    private boolean isInRange(Calendar from, Calendar until, int year, int month, int day) {
        if (from == null || until == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, from.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, from.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, from.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long millis1 = calendar.getTimeInMillis();

        calendar.set(Calendar.YEAR, until.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, until.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, until.get(Calendar.DAY_OF_MONTH));
        long millis2 = calendar.getTimeInMillis();
        calendar.set(until.get(Calendar.YEAR), until.get(Calendar.MONTH), until.get(Calendar.DAY_OF_MONTH));
        if (calendar.getTimeInMillis() > getCurrentTimeMillies()) {
            txtDayReturn.setText(getDayFormat(calendar.getTime()));
        }
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        long millis3 = calendar.getTimeInMillis();
        return millis1 <= millis3 && millis2 >= millis3;
    }

    private void doOnCalendarDayClicked(int year, int month, int day) {
        Calendar clickedOn = Calendar.getInstance();
        clickedOn.set(Calendar.YEAR, year);
        clickedOn.set(Calendar.MONTH, month);
        clickedOn.set(Calendar.DAY_OF_MONTH, day);
        clickedOn.set(Calendar.HOUR_OF_DAY, 0);
        clickedOn.set(Calendar.MINUTE, 0);
        clickedOn.set(Calendar.SECOND, 0);
        clickedOn.set(Calendar.MILLISECOND, 0);

        if (shouldClearAllSelected(clickedOn)) {
            from = null;
            until = null;
        } else if (shouldSetFrom(clickedOn)) {
            from = clickedOn;
            until = null;
        } else if (shouldSetUntil()) {
            until = clickedOn;
        }
    }

    private boolean shouldSetUntil() {
        return until == null;
    }

    private boolean shouldClearAllSelected(Calendar calendar) {
        return from != null && from.equals(calendar);
    }

    private boolean shouldSetFrom(Calendar calendar) {
        return from == null || until != null || isBefore(from, calendar);
    }

    private boolean isBefore(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, c2.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, c2.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, c2.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long millis = calendar.getTimeInMillis();

        calendar.set(Calendar.YEAR, c1.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, c1.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, c1.get(Calendar.DAY_OF_MONTH));
        long millis2 = calendar.getTimeInMillis();

        return millis < millis2;
    }

    private boolean isSelected(Calendar selected, int year, int month, int day) {
        if (selected == null) {
            return false;
        }
        //noinspection UnnecessaryLocalVariable
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selected.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, selected.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, selected.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long millis = calendar.getTimeInMillis();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        long millis2 = calendar.getTimeInMillis();
        return millis == millis2;
    }
}
