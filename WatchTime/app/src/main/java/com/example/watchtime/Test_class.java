package com.example.watchtime;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watchtime.source.JSON.JSONReader;
import com.example.watchtime.source.Object.Timer_data;
import com.example.watchtime.source.Object.Timezone;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Test_class extends AppCompatActivity implements Serializable {

    public Timer_data data;
    public TextView showdata;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        showdata = findViewById(R.id.test_data);

        /*

        data = (Timer_data)getIntent().getSerializableExtra("Timer_data");



        int totalTime = data.totalTime;
        int hour = totalTime/(60*60*1000);
        int minutes = (totalTime - (hour*(60*60*1000)))/(60*1000);
        int second = (totalTime - hour*(60*60*1000) - minutes*60*1000)/1000;



         */
        TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        TimeZone tz2 = TimeZone.getTimeZone("Asia/Tokyo");
        //int differ =  - tz.getOffset(new Date().getTime());

        //Time checkTime = new Time((int) (new Date().getTime()+tz.getOffset(new Date().getTime())));
        //showdata.setText(checkTime.hour+":"+checkTime.minute);
        //Time checkTime2 = new Time((int) Calendar.getInstance().getTime().getTime());

        Calendar c = Calendar.getInstance(tz);
        //showdata.setText(c.getTime().getHours()+":"+c.getTime().getMinutes()+":"+c.getTime().getSeconds() +"=="+(tz2.getOffset(new Date().getTime())/1000/60/60-tz.getOffset(new Date().getTime())/1000/60/60));

        List<Timezone> listcity_ingroup = getTimezoneData(R.raw.timezones,this);


        for (Timezone i : listcity_ingroup){
            for(Timezone.zones j : i.getZones()){
                Log.e("TimeZone",j.value +" "+j.getName());
            }
        }



    }

    private List<Timezone> getTimezoneData(int timezones, Test_class test_class) {
        JSONReader data = new JSONReader();
        List<Timezone> timezonesData = data.convertJsonToTimeZoneObject(R.raw.timezones, this);

        return timezonesData;
    }
}
