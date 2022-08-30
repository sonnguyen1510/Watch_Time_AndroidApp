package com.example.watchtime;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watchtime.resouce.Object.Timer_data;

import java.io.Serializable;

public class Test_class extends AppCompatActivity implements Serializable {

    public Timer_data data;
    public TextView showdata;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);


        data = (Timer_data)getIntent().getSerializableExtra("Timer_data");

        showdata = findViewById(R.id.test_data);

        int totalTime = data.getData().getTotalTime();
        int hour = totalTime/(60*60*1000);
        int minutes = (totalTime - (hour*(60*60*1000)))/(60*1000);
        int second = (totalTime - hour*(60*60*1000) - minutes*60*1000)/1000;

        showdata.setText(hour +":"+minutes+":"+second);





    }
}
