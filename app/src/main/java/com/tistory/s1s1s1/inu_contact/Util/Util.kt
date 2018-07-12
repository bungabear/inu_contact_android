/*
 * Copyright (c) 2018. Minjae Son
 */

package com.tistory.s1s1s1.inu_contact.Util

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class Util {
    companion object {
        fun hideKeyboard(activity : Activity){
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
        fun hideKeyboard(fragment : Fragment){
            val imm = fragment.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = fragment.view!!.rootView
            imm.hideSoftInputFromWindow(view.windowToken, 0);
            view.clearFocus();
        }
        fun hideKeyboard(editText: EditText){
            editText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }
    }
}
