package com.example.watchtime.source.ui.Time;

import static com.example.watchtime.resouce.global_variable.World_Clock_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watchtime.R;
import com.example.watchtime.resouce.function;
import com.example.watchtime.resouce.global_variable;
import com.example.watchtime.source.Database.DataStore;
import com.example.watchtime.source.Database.Timer.AlertSong;
import com.example.watchtime.resouce.Object.Timer_data;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Timer extends AppCompatActivity implements Serializable {
    /**
     *****MENU*****
     */
    public ImageView Timer ;
    public ImageView World_Clock;
    public ImageView Alarm;
    /**
     *********************************TIMER****************************
     */
    public Button start_timer;
    public Button cancel_timer;


    public Timer_data data ;
    public ProgressBar PercentTimeLeft;
    public TextView ShowTimeLeft;

    //Alert song
    public TextInputLayout alertsong;
    public AutoCompleteTextView alertsongChoosed;
    public List<String> ListSong;
    public ArrayAdapter<String> alertsongAdapter;
    public int DefaultSong;

    //time
    public LinearLayout timeSetting;
    public NumberPicker mHourPicker;
    public NumberPicker mMinutePicker;
    public NumberPicker mSecondPicker;

    private int mCurrentHour = 0; // 0-23
    private int mCurrentMinute = 0; // 0-59
    private int mCurrentSeconds = 0; // 0-59

    public static final NumberPicker.Formatter TWO_DIGIT_FORMATTER =
            new NumberPicker.Formatter() {

                @Override
                public String format(int value) {
                    return String.format("%02d", value);
                }
            };

    /**
     *********************************CONTACT TO SERVICE ****************************
     */
    //Use to get time data from service
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String datatime = intent.getStringExtra(global_variable.TimeData);
            startTimerLayoutChange();
            ShowTimeLeft.setText(datatime);

        }
    };


    /**---------------------------------------------ACTIVITY--------------------------------------*/
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.watchtime.source.ui.Time");
        registerReceiver(broadcastReceiver, intentFilter);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        //timePicker = new TimePicker(this);

        /**
         * --------------------------------MENU----------------------------
         */
        //Timer activity setup
        Timer = findViewById(R.id.Timer_button);
        Timer.setImageResource(R.drawable.ic_baseline_timer_red);
        Timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

            }
        });

        //world clock activity setup
        World_Clock = findViewById(R.id.world_clock_button);
        World_Clock .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),World_Clock_activity));
            }
        });

        //world clock activity setup
        Alarm = findViewById(R.id.alarm_button);
        Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),World_Clock_activity));
            }
        });

        /**
         *-------------------------------TIMER--------------------------
         */
        //-------------------------------SETUP--------------------------
        ShowTimeLeft = findViewById(R.id.ShowTimeLeft);
        timeSetting = findViewById(R.id.timeSetting);
        start_timer = findViewById(R.id.start_timer);
        cancel_timer = findViewById(R.id.Cancel_timer);
        ShowTimeLeft = findViewById(R.id.ShowTimeLeft);
        PercentTimeLeft = findViewById(R.id.PercentageOfTimeLeft);

        //------------------------------TIME PICKER--------------------

        mHourPicker= findViewById(R.id.hour);
        mMinutePicker= findViewById(R.id.minute);
        mSecondPicker= findViewById(R.id.seconds);

        setCurrentHour(0);
        setCurrentMinute(0);
        setCurrentSecond(0);

        // hour
        mHourPicker = (NumberPicker) findViewById(R.id.hour);
        mHourPicker.setMinValue(0);
        mHourPicker.setMaxValue(24);
        mHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mCurrentHour = newVal;
            }
        });

        // digits of minute
        mMinutePicker = (NumberPicker) findViewById(R.id.minute);
        mMinutePicker.setMinValue(0);
        mMinutePicker.setMaxValue(59);
        mMinutePicker.setFormatter(TWO_DIGIT_FORMATTER);
        mMinutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker spinner, int oldVal, int newVal) {
                mCurrentMinute = newVal;
            }
        });

        // digits of seconds
        mSecondPicker = (NumberPicker) findViewById(R.id.seconds);
        mSecondPicker.setMinValue(0);
        mSecondPicker.setMaxValue(59);
        mSecondPicker.setFormatter( TWO_DIGIT_FORMATTER);
        mSecondPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mCurrentSeconds = newVal;
            }
        });
        //------------------------------ALERT--------------------------
        //-----FIRST USING APP ------------
        if(DataStore.getInstance(this).alertSongQuery().getAllAlertsong().isEmpty()){
            List<AlertSong> CreateFristListSong = new ArrayList<>();
            CreateFristListSong.add(new AlertSong("Radar",R.raw.radar));
            CreateFristListSong.add(new AlertSong("Apex",R.raw.apex));

            for (AlertSong data:CreateFristListSong) {
                DataStore.getInstance(this).alertSongQuery().insertAlertSong(data);
            }
        }
        //-----SET DEFAULT ------------
        DefaultSong = R.raw.radar;

        //-----SETUP DROPDOWN ALERT SONG MENU--------------
        alertsong = findViewById(R.id.choose_alert_song);
        alertsongChoosed = findViewById(R.id.alertsong_choosed);
        ListSong = DataStore.getInstance(this).alertSongQuery().getAllAlertsong();

        alertsongAdapter = new ArrayAdapter<>(this,R.layout.alertsong_dropdownitem,ListSong);
        alertsongChoosed.setAdapter(alertsongAdapter);
        alertsongChoosed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                try {
                    DefaultSong = DataStore.getInstance(view.getContext()).alertSongQuery().getAlertSong(item);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //-------------------------------BUTTON-------------------------

        start_timer = findViewById(R.id.start_timer);
        start_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Change Layout
                startTimerLayoutChange();

                //String[] Time = inputTime.getText().toString().split(":");
                int hours = mCurrentHour;
                int minutes = mCurrentMinute;
                int seconds = mCurrentSeconds;

                data = new Timer_data();
                data.totalTime = function.Timer.toMilisecond(hours,minutes,seconds);
                data.hour = mCurrentHour;
                data.minute = mCurrentMinute;
                data.second = mCurrentSeconds;
                //alert song
                data.AlertSong = DefaultSong;


                startTimer();
                //onCountdown();
            }


        });


        cancel_timer = findViewById(R.id.Cancel_timer);
        cancel_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });


        
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
    //------------------------------------------LAYOUT CHANGE------------------------------------
    private void startTimerLayoutChange() {
        start_timer.setVisibility(View.GONE);
        alertsong.setVisibility(View.GONE);
        timeSetting.setVisibility(View.GONE);
        //OnCountDown
        cancel_timer.setVisibility(View.VISIBLE);
        PercentTimeLeft.setVisibility(View.VISIBLE);
        ShowTimeLeft.setVisibility(View.VISIBLE);
    }

    private void stopTimerLayoutChange() {
        start_timer.setVisibility(View.VISIBLE);
        alertsong.setVisibility(View.VISIBLE);
        timeSetting.setVisibility(View.VISIBLE);
        //OnCountDown
        cancel_timer.setVisibility(View.GONE);
        PercentTimeLeft.setVisibility(View.GONE);
        ShowTimeLeft.setVisibility(View.GONE);
    }

    private void ResetTime(){
        setCurrentHour(0);
        setCurrentMinute(0);
        setCurrentSecond(0);
    }

    //------------------------------------------TIME CHANGE------------------------------------
    private void setCurrentSecond(int currentSecond) {
        this.mCurrentSeconds = currentSecond;
        updateSecondsDisplay();
    }


    private void setCurrentMinute(int currentMinute) {
        this.mCurrentMinute = currentMinute;
        updateMinuteDisplay();
    }

    private void setCurrentHour(int currentHour) {
        this.mCurrentHour = currentHour;
        updateHourDisplay();
    }

    private void updateMinuteDisplay() {
        mMinutePicker.setValue(mCurrentMinute);
        //mOnTimeChangedListener.onTimeChanged(this, getCurrentHour(), getCurrentMinute(), getCurrentSeconds());
    }

    /**
     * Set the state of the spinners appropriate to the current second.
     */
    private void updateSecondsDisplay() {
        mSecondPicker.setValue(mCurrentSeconds);
        //mOnTimeChangedListener.onTimeChanged(this, getCurrentHour(), getCurrentMinute(), getCurrentSeconds());
    }
    /**
     * Set the state of the spinners appropriate to the current hour.
     */
    private void updateHourDisplay() {
        int currentHour = mCurrentHour;
        mHourPicker.setValue(currentHour);
    }





    //----------------------------------------SERVICE-------------------------------------
    private void stopTimer() {
        //DataStore.getInstance(this).timeCountdownQuery().deleteTimeLeft(data.getData().getID());
        stopTimerLayoutChange();
        stopService(new Intent(this,global_variable.TimerService));
        ResetTime();
    }



    private void startTimer() {
        Intent startTimer =new Intent(this,global_variable.TimerService);
        //Send time data
        //Log.e("",data.getData().getTotalTime()+"");
        startTimer.putExtra("Timer_data", (Serializable) data);
        startService(startTimer);
    }
}

/*
        inputTime = findViewById(R.id.inputTime);
        inputTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = 0;
                int minute = 0;


                MyTimePickerDialog mTimePicker = new MyTimePickerDialog(v.getContext(), new MyTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(com.example.watchtime.resouce.timepickerdialog.TimePicker view, int hourOfDay, int minute, int second) {
                        inputTime.setText(String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute) +
                                ":" + String.format("%02d", second));
                    }


                }, 0, 0, 0, true);
                mTimePicker.show();
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        * */
