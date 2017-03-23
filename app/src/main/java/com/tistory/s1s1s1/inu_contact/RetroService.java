package com.tistory.s1s1s1.inu_contact;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by seonilkim on 2017. 3. 23..
 */

public interface RetroService {

    @GET("contact")
    Call<JsonElement> contact();
}
