package com.tistory.s1s1s1.inu_contact.Network

import android.os.AsyncTask
import android.support.v4.app.Fragment
import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tistory.s1s1s1.inu_contact.Fragment.GroupFragment
import com.tistory.s1s1s1.inu_contact.LOG_TAG
import com.tistory.s1s1s1.inu_contact.RecyclerView.ContactAdapter
import com.tistory.s1s1s1.inu_contact.Util.DBHelper

class RetroAsync
(
//                        private val xProgressDialog: MyProgress?,
        private val fragment : Fragment,
        private val result: JsonArray,
        private val dbHelper: DBHelper) : AsyncTask<Unit, Unit, Unit>() {
    override fun onPreExecute() {
        super.onPreExecute()
        Log.d(LOG_TAG, "DB 입력 시작")
    }

    override fun doInBackground(vararg voids: Unit): Unit? {
        var json: JsonObject
        for (i in 0 until result.size()) {
            json = result.get(i).asJsonObject
            dbHelper.insert(json)
        }
        return null
    }

    override fun onPostExecute(aVoid: Unit?) {
        if(fragment is GroupFragment){
//            Log.d(LOG_TAG, "DB 입력 종료" + dbHelper.part.toString())
            fragment.mRV!!.adapter = ContactAdapter(dbHelper.part)
//            actionbar_tv_title.setText(R.string.app_name)
            fragment.mRV!!.adapter.notifyDataSetChanged()

//            if (actionbar_et_search.isFocused) {
//                actionbar_et_search.clearFocus()
//            }
        }
//        fragment..adapter = ContactAdapter(dbHelper.part)
//
//        xProgressDialog!!.dismiss()
    }
}


