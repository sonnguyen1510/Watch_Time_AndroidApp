package com.example.watchtime.TimeAPI.APIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient{
    public static Retrofit retrofit ;
    public static final String BASEURL = "https://timeapi.io/";

    public static Retrofit GetRetrofit(){
        if(retrofit== null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
