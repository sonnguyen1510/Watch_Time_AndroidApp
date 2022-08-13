package com.example.watchtime.ui.Time;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watchtime.Object.Time;
import com.example.watchtime.R;

public class Timer extends AppCompatActivity {
    /**
     *****MENU*****
     */
    public ImageView Timer ;
    public ImageView World_Clock;
    public ImageView Alarm;
    /**
     *********************************TIMER****************************
     */


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);
        /**
         * --------------------------------MENU----------------------------
         */
        //Timer activity setup
        Timer = findViewById(R.id.Timer_button);
        Timer.setImageResource(R.drawable.ic_baseline_timer_red);
        Timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //world clock activity setup
        World_Clock = findViewById(R.id.world_clock_button);
        World_Clock .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //world clock activity setup
        Alarm = findViewById(R.id.alarm_button);
        Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /**
         *-------------------------------TIMER--------------------------
         */
        
    }
}
