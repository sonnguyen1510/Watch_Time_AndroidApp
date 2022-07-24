package com.example.watchtime.TimeAPI.APICall;

import com.example.watchtime.Object.Time;
import com.example.watchtime.TimeAPI.APIService.APIInterface;
import com.example.watchtime.TimeAPI.APIService.RetrofitClient;

import retrofit2.Call;

public class Time_APICall {
    public static Call<Time> getTime(String timeZone){
        APIInterface apiInterface = RetrofitClient.GetRetrofit().create(APIInterface.class);
        return apiInterface.getTimes(timeZone);
    }
}
