package com.tistory.s1s1s1.inu_contact.Network

import android.content.Context
import android.os.AsyncTask
import android.support.v7.widget.DefaultItemAnimator

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tistory.s1s1s1.inu_contact.DBHelper
import com.tistory.s1s1s1.inu_contact.RecyclerView.ContactAdapter

import com.tistory.s1s1s1.inu_contact.MainActivity.actionbar_et_search
import com.tistory.s1s1s1.inu_contact.MainActivity.actionbar_tv_title
import com.tistory.s1s1s1.inu_contact.MainActivity.main_rv
import com.tistory.s1s1s1.inu_contact.MainActivity.rv_level
import com.tistory.s1s1s1.inu_contact.Progress.MyProgress
import com.tistory.s1s1s1.inu_contact.R
import java.lang.ref.WeakReference


/**
 * Created by seonilkim on 2017. 3. 23..
 */

class RetroAsync
                (private val context: Context,
                 private val xProgressDialog: MyProgress,
                 val result: JsonArray,
                 private val dbHelper: DBHelper) : AsyncTask<Void, Void, Void>() {

    override fun onPreExecute() {
        //        if(singleton == null) singleton.getInstance(context);
    }

    override fun doInBackground(vararg voids: Void): Void? {
        var json: JsonObject
        for (i in 0 until result!!.size()) {
            json = result!!.get(i).asJsonObject
            dbHelper.insert(json)
        }

        return null
    }

    override fun onPostExecute(aVoid: Void) {
        main_rv.adapter = ContactAdapter(context, dbHelper.part)
        main_rv.itemAnimator = DefaultItemAnimator()
        actionbar_tv_title.setText(R.string.app_name)
        rv_level = 0
        if (actionbar_et_search.isFocused) {
            actionbar_et_search.clearFocus()
        }
        xProgressDialog!!.dismiss()
    }
}


