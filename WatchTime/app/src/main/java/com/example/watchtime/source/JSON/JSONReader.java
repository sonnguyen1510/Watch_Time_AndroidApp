package com.example.watchtime.source.JSON;

import android.content.Context;

import com.example.watchtime.source.Object.Timezone;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JSONReader{
    public List<Timezone> convertJsonToTimeZoneObject(int jsonfile, Context context){
        InputStream inputStream = context.getResources().openRawResource(jsonfile);

        String jsonString = "";

        try {
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();

            jsonString=  new String(data,"UTF-8");


        } catch (IOException e) {
            e.printStackTrace();
        }


        return new Gson().fromJson(jsonString, new TypeToken<List<Timezone>>(){}.getType());
    }
}
