package com.tistory.s1s1s1.inu_contact.Network

import com.google.gson.JsonElement

import retrofit2.Call
import retrofit2.http.GET

interface RetroService {

    @GET("contact")
    fun contact(): Call<JsonElement>
}
