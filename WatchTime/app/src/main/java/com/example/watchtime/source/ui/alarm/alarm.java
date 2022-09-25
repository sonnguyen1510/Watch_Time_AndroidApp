package com.example.watchtime.source.ui.alarm;

import static com.example.watchtime.resouce.global_variable.AlarmData;
import static com.example.watchtime.resouce.global_variable.World_Clock_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchtime.R;
import com.example.watchtime.source.Database.Alarm.Alarm;
import com.example.watchtime.source.Database.DataStore;
import com.example.watchtime.source.ui.alarm.alarm_view.list_alarm;


import java.util.List;

public class alarm extends AppCompatActivity {
    /**
     *****MENU*****
     */
    public ImageView Timer ;
    public ImageView World_Clock;
    public ImageView Alarm;
    /**

    /**
     * -------------------ALARM-----------------------
     */
    public list_alarm alarmAdapter;
    public List<Alarm> alarmData;
    public RecyclerView AlarmView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

        AlarmView = findViewById(R.id.alarm_list);
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
         ------------------------------ALARM--------------------------------------
         */
        //----------SHOW ALARM DATA-----------
        try {
            alarmData = DataStore.getInstance(this).alarmListQuery().getAllAlarm();
            alarmAdapter  = new list_alarm(alarmData,this);
            AlarmView.setAdapter(alarmAdapter);
            AlarmView.setLayoutManager(new LinearLayoutManager(this));
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"Can't get alarm data", Toast.LENGTH_LONG);
        }

        //--------------

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_item:
                AddAlarm();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void AddAlarm() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup_example, null, false),100,100, true);
        //pw.showAtLocation(, Gravity.CENTER, 0, 0);
    }
}
