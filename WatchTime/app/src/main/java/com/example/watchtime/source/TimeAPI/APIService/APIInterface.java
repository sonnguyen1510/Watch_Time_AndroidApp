package com.example.watchtime.source.TimeAPI.APIService;

import com.example.watchtime.source.TimeAPI.Timezone;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    /**
     * TIME
     */
    //LINK API : https://timeapi.io/api/Time/current/zone?timeZone=Asia/Saigon
    @GET("/timezone")
    Call<Timezone> getTimes( @Query("apiKey") String ApiKey,@Query("tz") String timeZone );
}
