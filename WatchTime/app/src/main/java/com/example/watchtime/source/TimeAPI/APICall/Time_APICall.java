package com.example.watchtime.source.TimeAPI.APICall;

import com.example.watchtime.source.Object.Time;
import com.example.watchtime.source.TimeAPI.APIService.APIInterface;
import com.example.watchtime.source.TimeAPI.APIService.RetrofitClient;

import retrofit2.Call;

public class Time_APICall {
    public static Call<Time> getTime(String timeZone){
        APIInterface apiInterface = RetrofitClient.GetRetrofit().create(APIInterface.class);
        return apiInterface.getTimes(timeZone);
    }
}
