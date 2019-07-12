package com.example.calendarcustom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    SeekBar skbReceiveCar, skbReturnCar;
    View viewReceive, viewReturn;
    TextView txtTimeReceive, txtTimeReturn, txtHourReturn, txtHourReceive;
    String time_hour = "";
    String time_minutes = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        viewReceive = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_seekbar_thumb, null, false);
        viewReturn = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_seekbar_thumb_1, null, false);
        txtTimeReceive = viewReceive.findViewById(R.id.tvProgressReceive);
        txtTimeReturn = viewReturn.findViewById(R.id.tvProgressReturn);
        viewReceive.bringToFront();
        viewReturn.bringToFront();
        txtTimeReceive.setEnabled(true);
        txtTimeReturn.setEnabled(true);

        seekBarChangeListener(skbReceiveCar, txtTimeReceive, txtHourReceive);
        seekBarChangeListener(skbReturnCar, txtTimeReturn, txtHourReturn);

    }

    private void initView() {
        skbReceiveCar = findViewById(R.id.time_receive_car);
        skbReturnCar = findViewById(R.id.time_return_car);
        txtHourReceive = findViewById(R.id.hour_receive);
        txtHourReturn = findViewById(R.id.hour_return);
        txtHourReturn.setText("00:00");
        txtHourReceive.setText("00:00");
        txtHourReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
            }
        });
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


}
