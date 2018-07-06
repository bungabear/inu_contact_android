package com.tistory.s1s1s1.inu_contact

import com.google.gson.JsonParser
import com.tistory.s1s1s1.inu_contact.Network.RetroService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object Singleton {
    // Retrofit
    private val retrofit : Retrofit= Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build()
    val retrofitService : RetroService = retrofit.create(RetroService::class.java)

    // DB
    val DB_VERSION = 1
    var CURRENT_PART = ""
    val markerJson = JsonParser().parse("[{\"title\":\"대학본부\",\"lat\":37.3767741,\"log\":126.63464720000002},{\"title\":\"교수회관\",\"lat\":37.3774963,\"log\":126.63373060000004},{\"title\":\"홍보관\",\"lat\":37.377315,\"log\":126.633953},{\"title\":\"정보전산원/정보기술교육원\",\"lat\":37.376385,\"log\":126.635552},{\"title\":\"자연과학대학\",\"lat\":37.375619,\"log\":126.634624},{\"title\":\"학산도서관\",\"lat\":37.375073,\"log\":126.634124},{\"title\":\"정보기술대학\",\"lat\":37.374438,\"log\":126.63362},{\"title\":\"공과대학\",\"lat\":37.373534,\"log\":126.632783},{\"title\":\"공동실험실습관\",\"lat\":37.372711,\"log\":126.633435},{\"title\":\"게스트하우스\",\"lat\":37.372813,\"log\":126.631783},{\"title\":\"복지회관\",\"lat\":37.37436,\"log\":126.631794},{\"title\":\"컨벤션센터\",\"lat\":37.375213,\"log\":126.632529},{\"title\":\"사회과학대학/법과대학\",\"lat\":37.375997,\"log\":126.633237},{\"title\":\"동북아경제통상대학/경영대학/물류대학원\",\"lat\":37.376526,\"log\":126.632883},{\"title\":\"인문대학/어학원\",\"lat\":37.375656,\"log\":126.631982},{\"title\":\"예체능대학\",\"lat\":37.374761,\"log\":126.631285},{\"title\":\"학생회관\",\"lat\":37.374173,\"log\":126.630695},{\"title\":\"기숙사\",\"lat\":37.37384,\"log\":126.629869},{\"title\":\"국제교류관\",\"lat\":37.374548,\"log\":126.630234},{\"title\":\"스포츠센터\",\"lat\":37.374991,\"log\":126.629655},{\"title\":\"체 육관\",\"lat\":37.375485,\"log\":126.630234},{\"title\":\"학군단\",\"lat\":37.375894,\"log\":126.63076},{\"title\":\"공연장\",\"lat\":37.377863,\"log\":126.632444},{\"title\":\"전망타워\",\"lat\":37.375919,\"log\":126.635716},{\"title\":\"어린이집\",\"lat\":37.375271,\"log\":126.636049},{\"title\":\"온실\",\"lat\":37.375194,\"log\":126.635566},{\"title\":\"제2공동실험실습관\",\"lat\":37.371946,\"log\":126.633356},{\"title\":\"도시과학대학\",\"lat\":37.371801,\"log\":126.633023},{\"title\":\"생명공학부\",\"lat\":37.372586,\"log\":126.631327}]").asJsonArray
}