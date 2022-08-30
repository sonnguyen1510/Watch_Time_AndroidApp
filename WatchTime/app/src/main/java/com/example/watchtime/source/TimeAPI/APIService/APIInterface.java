package com.example.watchtime.source.TimeAPI.APIService;

import com.example.watchtime.resouce.Object.Time;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    /**
     * TIME
     */
    //LINK API : https://timeapi.io/api/Time/current/zone?timeZone=Asia/Saigon
    @GET("api/Time/current/zone")
    Call<Time> getTimes(@Query("timeZone") String timeZone);
}
