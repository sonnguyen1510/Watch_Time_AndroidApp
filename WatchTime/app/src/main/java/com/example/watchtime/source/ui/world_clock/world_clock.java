package com.example.watchtime.source.ui.world_clock;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.watchtime.source.Object.Time;
import com.example.watchtime.R;
import com.example.watchtime.source.TimeAPI.APICall.Time_APICall;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class world_clock extends AppCompatActivity {
    private TextView Hour ;
    private TextView Minute;
    private TextView Second;
    private TextView MilliSecond;
    private Button GetTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hour = findViewById(R.id.Time_hour);
        Minute= findViewById(R.id.Time_Minute);
        Second= findViewById(R.id.Time_Second);
        MilliSecond= findViewById(R.id.Time_MiliSecond);
        GetTime= findViewById(R.id.Time_GetTime);

        GetTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Time_APICall.getTime("Asia/Saigon").enqueue(new Callback<Time>() {
                        @Override
                        public void onResponse(Call<Time> call, Response<Time> response) {
                            Time time = response.body();
                            if(time!=null ){
                                Hour.setText(time.getHour()+"");
                                Minute.setText(time.getMinute()+"");
                                Second.setText(time.getSeconds()+"");
                                MilliSecond.setText(time.getMilliSeconds()+"");
                            }
                        }
                        @Override
                        public void onFailure(Call<Time> call, Throwable t) {
                            Log.e("Fail","Get time unsuccess");
                        }
                    });
                    SystemClock.sleep(300);

            }
        });

    }

}