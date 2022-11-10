package com.example.watchtime;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watchtime.source.Database.DataStore;
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

        if(DataStore.getInstance(this).worldClockListQuery().getWorldClockByRegion("hkjtr") == null){
            showdata.setText("isNotExist");
        }
        else{
            showdata.setText("IsExist");
        }





    }


}
