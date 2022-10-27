package com.example.watchtime.source.TimeAPI.APICall;

import com.example.watchtime.source.GlobalData.global_variable;
import com.example.watchtime.source.TimeAPI.APIService.APIInterface;
import com.example.watchtime.source.TimeAPI.APIService.RetrofitClient;
import com.example.watchtime.source.TimeAPI.Timezone;

import retrofit2.Call;

public class Time_APICall {
    public static Call<Timezone> getTime(String timeZone){
        APIInterface apiInterface = RetrofitClient.GetRetrofit().create(APIInterface.class);
        return apiInterface.getTimes(global_variable.APIkey, timeZone);
    }
}
