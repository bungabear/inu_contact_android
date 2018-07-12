/*
 * Copyright (c) 2018. Minjae Son
 */

package com.tistory.s1s1s1.inu_contact.Util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import org.jsoup.Jsoup
import java.io.IOException

class Util {
    companion object {
        fun hideKeyboard(activity : Activity){
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            view.clearFocus()
        }
        fun hideKeyboard(fragment : Fragment){
            val imm = fragment.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = fragment.view!!.rootView
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
        fun hideKeyboard(editText: EditText){
            editText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        fun networkCheck(context : Context): Boolean {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = manager.getActiveNetworkInfo()
            return  if (activeNetwork != null) true else false

        }

        fun getMarketVersion(packageName: String): String? {
            try {
                val doc = Jsoup.connect("https://play.google.com/store/apps/details?id=$packageName").get()
                val version = doc.select(".content")
                for (mElement in version) {
                    if (mElement.attr("itemprop") == "softwareVersion") {
                        return mElement.text().trim { it <= ' ' }
                    }
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return null
        }
    }
}
