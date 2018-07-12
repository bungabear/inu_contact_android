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
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.tistory.s1s1s1.inu_contact.Activity.MainActivity
import com.tistory.s1s1s1.inu_contact.Model.Contact
import com.tistory.s1s1s1.inu_contact.R
import com.tistory.s1s1s1.inu_contact.RecyclerView.ContactAdapter
import com.tistory.s1s1s1.inu_contact.Util.DBHelper
import com.tistory.s1s1s1.inu_contact.Util.Singleton
import kotlinx.android.synthetic.main.fragment_group.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GroupFragment : Fragment(){

    private lateinit var mLayout : ViewGroup
    private var callback : (() -> ArrayList<Contact>?)? = null
    var mRV : RecyclerView? = null
    var part = ""
    var lateAdapter : ContactAdapter? = null

    companion object {
        fun newInstance(part: String ="", callback: (() -> ArrayList<Contact>?)? = null) : GroupFragment{
            val fragment = GroupFragment()
            fragment.callback = callback
            fragment.part = part
            return fragment
        }
    }

    private var isFirst = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(isFirst) {
            MainActivity.actionBarTextView.setText("")
            MainActivity.actionBarSearchView.onEditorAction(EditorInfo.IME_ACTION_DONE)

            mLayout = inflater.inflate(R.layout.fragment_group, container, false) as ViewGroup
            mRV = mLayout.fragment_group_rv
            mRV!!.layoutManager = LinearLayoutManager(context)

            mRV!!.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
//            getDB(this)
            mRV!!.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.group_recycler_layoutanimation);
            if(callback != null){
                mRV!!.adapter = ContactAdapter(callback?.invoke()!!)
            }

            isFirst = false
        }
        return mLayout
    }

    fun reset(){
        mRV!!.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.group_recycler_layoutanimation);
        if(callback != null){
            mRV!!.adapter = ContactAdapter(callback?.invoke()!!)
        }
    }

    override fun onResume() {
        super.onResume()
        MainActivity.actionBarTextView.text = if(part == "") getString(R.string.app_name) else if(part.length >= 13) part.substring(0, 12) + "..." else part
        (mRV!!.adapter as ContactAdapter).filter.filter("")
    }

    private fun getDB(fragment : Fragment) {
        val dbHelper = DBHelper.instance
        Singleton.retrofitService.contact().enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                dbHelper!!.delete()
                val result = response.body()!!.asJsonArray
                var json: JsonObject
                for (i in 0 until result.size()) {
                    json = result.get(i).asJsonObject
                    dbHelper.insert(json)
                }
                mRV!!.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.group_recycler_layoutanimation);
                mRV!!.adapter = ContactAdapter(dbHelper.part)
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("LOG_TAG", "RETROFIT FAILED")
                mRV!!.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.group_recycler_layoutanimation);
                mRV!!.adapter = ContactAdapter(dbHelper!!.part)
                Snackbar.make(mLayout, "데이터 업데이트에 실패하였습니다.\n새로고침을 눌러주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}