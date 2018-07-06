package com.tistory.s1s1s1.inu_contact.Network

import android.os.AsyncTask
import android.support.v7.widget.DefaultItemAnimator
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tistory.s1s1s1.inu_contact.DBHelper
import com.tistory.s1s1s1.inu_contact.MainActivity.*
import com.tistory.s1s1s1.inu_contact.Progress.MyProgress
import com.tistory.s1s1s1.inu_contact.R
import com.tistory.s1s1s1.inu_contact.RecyclerView.ContactAdapter

class RetroAsync
                (private val xProgressDialog: MyProgress,
                 private val result: JsonArray,
                 private val dbHelper: DBHelper) : AsyncTask<Unit, Unit, Unit>() {

    override fun doInBackground(vararg voids: Unit): Unit? {
        var json: JsonObject
        for (i in 0 until result.size()) {
            json = result.get(i).asJsonObject
            dbHelper.insert(json)
        }
        return null
    }

    override fun onPostExecute(aVoid: Unit?) {
        main_rv.adapter = ContactAdapter(dbHelper.part)
        main_rv.itemAnimator = DefaultItemAnimator()
        actionbar_tv_title.setText(R.string.app_name)
        main_rv.adapter.notifyDataSetChanged()
        rv_level = 0
        if (actionbar_et_search.isFocused) {
            actionbar_et_search.clearFocus()
        }
        xProgressDialog.dismiss()
    }
}


