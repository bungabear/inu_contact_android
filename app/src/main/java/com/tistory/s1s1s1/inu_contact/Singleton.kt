package com.tistory.s1s1s1.inu_contact

import com.tistory.s1s1s1.inu_contact.Network.RetroService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object Singleton {
    // Retrofit
    val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build()
    val retrofitSerice = retrofit.create(RetroService::class.java)

    // DB
    val DB_VERSION = 1
    var CURRENT_PART = ""
}