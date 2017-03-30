package com.tistory.s1s1s1.inu_contact;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by seonilkim on 2017. 3. 23..
 */

public class Singleton {
    private volatile static Singleton instance;

    private static Retrofit retrofit;
    private static RetroService retroService;

    private static final String BASE_URL = "";
    private static final int DB_VERSION = 1;

    public static String CURRENT_PART = "";

    private Singleton(Context context){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retroService = retrofit.create(RetroService.class);
    }

    public static Singleton getInstance (Context context){
        if(instance == null) {
            synchronized (Singleton.class) {
                if(instance == null) {
                    instance = new Singleton(context);
                }
            }
        }
        return instance;
    }

    public static Retrofit getRetrofit(){
        return retrofit;
    }

    public static RetroService getRetroService(){
        return retroService;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getCurrentPart() { return CURRENT_PART; }

    public static int getDbVersion() { return DB_VERSION; }

}
