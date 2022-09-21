package com.example.watchtime.source.ui.Time;

import static com.example.watchtime.resouce.global_variable.World_Clock_activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.watchtime.R;
import com.example.watchtime.resouce.function;
import com.example.watchtime.resouce.global_variable;
import com.example.watchtime.source.Database.DataStore;
import com.example.watchtime.source.Database.Timer.timeCountdown;
import com.example.watchtime.resouce.Object.Timer_data;

import java.io.Serializable;

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

    public LinearLayout timeSetting;
    public NumberPicker mHourPicker;
    public NumberPicker mMinutePicker;
    public NumberPicker mSecondPicker;

    private int mCurrentHour = 0; // 0-23
    private int mCurrentMinute = 0; // 0-59
    private int mCurrentSeconds = 0; // 0-59


    /**
     *********************************CONNECT TO SERVICE ****************************
     */
    //Use to get time data from service
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String datatime = intent.getStringExtra(global_variable.BroadcaseIntentName);

            //timeCountdown timeCountdown= DataStore.getInstance(Timer.this).timeCountdownQuery().getTimeLeft(global_variable.TimerID);
            //data = new Timer_data(timeCountdown);
            //int[] time = function.Timer.getTime(timeCountdown.getTimeLeft());

            //data.second = time[2];
            //data.minute = time[1];
            //data.hour = time[0];
            startTimerLayoutChange();
            ShowTimeLeft.setText(datatime);

        }
    };




    public static final NumberPicker.Formatter TWO_DIGIT_FORMATTER =
            new NumberPicker.Formatter() {

                @Override
                public String format(int value) {
                    // TODO Auto-generated method stub
                    return String.format("%02d", value);
                }
            };

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

        /**
         if(DataStore.getInstance(this).timeCountdownQuery().getTimeLeft(global_variable.TimerID)!=null){
         timeCountdown timeCountdown= DataStore.getInstance(Timer.this).timeCountdownQuery().getTimeLeft(global_variable.TimerID);
         data = new Timer_data(timeCountdown);
         int[] time = function.Timer.getTime(timeCountdown.getTimeLeft());
         data.second = time[2];
         data.minute = time[1];
         data.hour = time[0];

         startTimerLayoutChange();
         onCountdown();


         }
         */


        //------------------------------TIME PICKER--------------------

        mHourPicker= findViewById(R.id.hour);
        mMinutePicker= findViewById(R.id.minute);
        mSecondPicker= findViewById(R.id.seconds);

        setCurrentHour(0);//cal.get(Calendar.HOUR_OF_DAY)
        setCurrentMinute(0);//cal.get(Calendar.MINUTE)
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

                int totalTime =  toMilisecond(hours,minutes,seconds);
                timeCountdown timeCountdown =new timeCountdown(global_variable.TimerID,totalTime,totalTime);
                data = new Timer_data(timeCountdown);

                data.hour = mCurrentHour;
                data.minute = mCurrentMinute;
                data.second = mCurrentSeconds;
                
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

    private void startTimerLayoutChange() {
        start_timer.setVisibility(View.GONE);

        timeSetting.setVisibility(View.GONE);
        //OnCountDown
        cancel_timer.setVisibility(View.VISIBLE);
        PercentTimeLeft.setVisibility(View.VISIBLE);
        ShowTimeLeft.setVisibility(View.VISIBLE);
    }

    private void stopTimerLayoutChange() {
        start_timer.setVisibility(View.VISIBLE);

        timeSetting.setVisibility(View.VISIBLE);
        //OnCountDown
        cancel_timer.setVisibility(View.GONE);
        PercentTimeLeft.setVisibility(View.GONE);
        ShowTimeLeft.setVisibility(View.GONE);
    }


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



    private int toMilisecond(int hours, int minutes, int second) {
        return (hours*60*60 + minutes*60 +second)*1000;
    }

    private void stopTimer() {
        //DataStore.getInstance(this).timeCountdownQuery().deleteTimeLeft(data.getData().getID());
        stopTimerLayoutChange();
        stopService(new Intent(this,global_variable.TimerService));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    private void startTimer() {
        Intent startTimer =new Intent(this,global_variable.TimerService);
        //Send time data
        Log.e("",data.getData().getTotalTime()+"");
        startTimer.putExtra("Timer_data", (Serializable) data);
        //startActivity(startTimer);
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
