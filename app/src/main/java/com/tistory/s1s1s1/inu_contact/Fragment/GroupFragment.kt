/*
 * Copyright (c) 2018. Minjae Son
 */

package com.tistory.s1s1s1.inu_contact.Fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonElement
import com.tistory.s1s1s1.inu_contact.Network.RetroAsync
import com.tistory.s1s1s1.inu_contact.R
import com.tistory.s1s1s1.inu_contact.Util.DBHelper
import com.tistory.s1s1s1.inu_contact.Util.Singleton
import kotlinx.android.synthetic.main.fragment_group.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupFragment : Fragment(){
//        main_rv = findViewById<View>(R.id.main_rv) as RecyclerView
//        main_rv.layoutManager = LinearLayoutManager(this)
//        main_rv.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    private lateinit var mLayout : ViewGroup
    lateinit var mRV : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mLayout = inflater.inflate(R.layout.fragment_group, container, false) as ViewGroup
        mRV = mLayout.fragment_group_rv
        mRV.layoutManager = LinearLayoutManager(context)
        mRV.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        getDB(this)

        return mLayout
    }



    private fun getDB(fragment : Fragment) {
//        if (xProgressDialog != null && !xProgressDialog.isShowing()) xProgressDialog.show()

        Singleton.retrofitService.contact().enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                //                progressDialog.dismiss();
                Log.d("LOG_TAG", "RETROFIT SUCCESSED")
                val dbHelper = DBHelper.instance
                dbHelper!!.delete()
                //                Log.d("LOG_TAG", response.toString());
                val result = response.body()!!.asJsonArray
                //                Log.d("LOG_TAG", dbHelper.toString());
//                val retroAsync = RetroAsync(xProgressDialog, result!!, dbHelper!!)
                val retroAsync = RetroAsync(fragment, result, dbHelper)
                retroAsync.execute()
                Log.d("LOG_TAG", "Success, count: " + dbHelper.dbCount)
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("LOG_TAG", "RETROFIT FAILED")
//                xProgressDialog!!.dismiss()
                Snackbar.make(mLayout, "데이터 업데이트에 실패하였습니다.\n새로고침을 눌러주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}